package io.github.lazyengineer.castaway.androidApp.viewmodel

import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaBrowserCompat.SubscriptionCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.lazyengineer.castaway.androidApp.parser.FeedParser.toFeedData
import io.github.lazyengineer.castaway.androidApp.view.MediaPlayerFragment
import io.github.lazyengineer.castaway.shared.entity.Episode
import io.github.lazyengineer.castaway.shared.entity.FeedData
import io.github.lazyengineer.castaway.shared.entity.PlaybackPosition
import io.github.lazyengineer.castaway.shared.usecase.GetFeedUseCase
import io.github.lazyengineer.castawayplayer.MediaServiceClient
import io.github.lazyengineer.castawayplayer.extention.isPlaying
import io.github.lazyengineer.castawayplayer.service.Constants.MEDIA_DESCRIPTION_EXTRAS_START_PLAYBACK_POSITION_MS
import io.github.lazyengineer.castawayplayer.service.Constants.MEDIA_ROOT_ID
import io.github.lazyengineer.castawayplayer.source.MediaData
import io.github.lazyengineer.feedparser.FeedParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParserFactory

class MainViewModel constructor(
	private val mediaServiceClient: MediaServiceClient,
	private val getFeedUseCase: GetFeedUseCase,
) : ViewModel() {

    private val subscriptionCallback = object : SubscriptionCallback() {
        override fun onChildrenLoaded(
            parentId: String,
            children: MutableList<MediaItem>
        ) {
            super.onChildrenLoaded(parentId, children)
            val items = children.map {
                Episode(
                    id = it.mediaId!!,
                    title = it.description.title.toString(),
                    subTitle = it.description.subtitle.toString(),
                    description = it.description.description.toString(),
                    audioUrl = it.description.mediaUri.toString(),
                    imageUrl = it.description.iconUri.toString(),
                    author = "",
                    playbackPosition = PlaybackPosition(
                        position = it.description.extras?.getLong(
                            MEDIA_DESCRIPTION_EXTRAS_START_PLAYBACK_POSITION_MS
                        ) ?: 0
                    ),
                    isPlaying = playingState(it.mediaId!!),
                )
            }

            _feed.postValue(FeedData(url = TEST_URL, title = "Android Backstage", episodes = items))
        }
    }

    private val _feed = MutableLiveData<FeedData>()
    val feed: LiveData<FeedData>
        get() = _feed

    private val _updatedEpisodes = MutableLiveData<List<Episode>>()
    val updatedEpisodes: LiveData<List<Episode>>
        get() = _updatedEpisodes

    private val _currentEpisode = MutableLiveData<Episode>()
    val currentEpisode: LiveData<Episode>
        get() = _currentEpisode

    private val _navigateToFragment = MutableLiveData<Fragment>()
    val navigateToFragment: LiveData<Fragment>
        get() = _navigateToFragment

    init {
        subscribeToMediaService()
        collectPlaybackState()
        collectPlaybackPositions()
        collectNowPlaying()
    }

    private fun collectPlaybackState() {
        viewModelScope.launch {
            mediaServiceClient.playbackState.collect {
                feed.value?.let { feedData ->
                    _feed.postValue(feed.value?.copy(episodes = feedData.episodes.map { episode ->
                        episode.copy(isPlaying = playingState(episode.id))
                    }))
                }
            }
        }
    }

    private fun collectPlaybackPositions() {
        viewModelScope.launch {
            mediaServiceClient.playbackPosition.collect { position ->
                feed.value?.postCurrentWithUpdatedPosition(position)
            }
        }
    }

    private fun collectNowPlaying() {
        viewModelScope.launch {
            mediaServiceClient.nowPlaying.collect { mediaData ->
                val feedEpisode = feed.value?.episodes?.firstOrNull { episode ->
                    mediaData.mediaId == episode.id
                }

                feedEpisode?.let { _currentEpisode.postValue(it) }
            }
        }
    }

    private fun FeedData.postCurrentWithUpdatedPosition(position: Long) {
        val episode = this.nowPlayingEpisode()

        episode?.let {
            _updatedEpisodes.postValue(
                listOf(it.withUpdatedPosition(position))
            )
        }
    }

    private fun FeedData.nowPlayingEpisode(): Episode? {
        return this.episodes.find { episode ->
            episode.id == mediaServiceClient.nowPlaying.value.mediaId
        }
    }

    private fun Episode.withUpdatedPosition(position: Long): Episode {
        return this.copy(
            playbackPosition = PlaybackPosition(
                position = position,
                duration = mediaServiceClient.nowPlaying.value.duration
            )
        )
    }

    fun fetchFeed() {
        viewModelScope.launch {
            fetchFeedFromUrl(TEST_URL)
        }
    }

    private suspend fun fetchFeedFromUrl(url: String) {
        withContext(Dispatchers.IO) {
            getFeedUseCase(
                url,
                onSuccess = {
                    val factory = XmlPullParserFactory.newInstance()
                    val xmlPullParser = factory.newPullParser()
                    val feed = FeedParser.parseFeed(it, xmlPullParser)
                    val feedData = feed.toFeedData(TEST_URL)

                    prepareMediaData(feedData.episodes)
                },
                onError = {},
            )
        }
    }

    private fun prepareMediaData(episodes: List<Episode>) {
        if (mediaServiceClient.isConnected.value) {
            mediaServiceClient.prepare {
                episodes.mapToMediaData()
            }
        }
    }

    private fun List<Episode>.mapToMediaData() = this.map {
        MediaData(
            mediaId = it.id,
            mediaUri = it.audioUrl,
            displayTitle = it.title,
            displayIconUri = it.imageUrl,
            displayDescription = it.description,
            displaySubtitle = it.subTitle ?: "",
            playbackPosition = it.playbackPosition.position,
            duration = it.playbackPosition.duration,
        )
    }

    private fun subscribeToMediaService() {
        viewModelScope.launch {
            mediaServiceClient.subscribe(MEDIA_ROOT_ID, subscriptionCallback)
        }
    }

    fun mediaItemClicked(clickedItemId: String) {
        if (mediaServiceClient.isConnected.value) {
            mediaServiceClient.playMediaId(clickedItemId)
        }
    }

    fun episodeClicked(clickedItem: Episode) {
        if (mediaServiceClient.isConnected.value) {
            _navigateToFragment.value = MediaPlayerFragment.newInstance(clickedItem)

            if (!playingState(clickedItem.id)) {
                mediaServiceClient.playMediaId(clickedItem.id)
            }
        }
    }

    fun forwardCurrentItem() {
        if (mediaServiceClient.isConnected.value) {
            mediaServiceClient.fastForward()
        }
    }

    fun replayCurrentItem() {
        if (mediaServiceClient.isConnected.value) {
            mediaServiceClient.rewind()
        }
    }

    fun skipToPrevious() {
        if (mediaServiceClient.isConnected.value) {
            mediaServiceClient.skipToPrevious()
        }
    }

    fun skipToNext() {
        if (mediaServiceClient.isConnected.value) {
            mediaServiceClient.skipToNext()
        }
    }

    fun seekTo(positionMillis: Long) {
        if (mediaServiceClient.isConnected.value) {
            mediaServiceClient.seekTo(positionMillis)
        }
    }

    fun playbackSpeed(speed: Float) {
        if (mediaServiceClient.isConnected.value) {
            mediaServiceClient.speed(speed)
        }
    }

    private fun playingState(mediaId: String): Boolean {
        val isActive = mediaId == mediaServiceClient.nowPlaying.value.mediaId
        val isPlaying = mediaServiceClient.playbackState.value.isPlaying
        return when {
            !isActive -> false
            isPlaying -> true
            else -> false
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaServiceClient.unsubscribe(MEDIA_ROOT_ID, subscriptionCallback)
    }

    companion object {
        private const val TAG = "MainViewModel"
        const val TEST_URL = "https://feeds.feedburner.com/blogspot/androiddevelopersbackstage"
    }
}