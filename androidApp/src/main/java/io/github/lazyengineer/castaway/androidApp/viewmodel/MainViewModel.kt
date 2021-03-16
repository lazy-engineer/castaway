package io.github.lazyengineer.castaway.androidApp.viewmodel

import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaBrowserCompat.SubscriptionCallback
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.lazyengineer.castaway.androidApp.usecase.StoreAndGetFeedUseCase
import io.github.lazyengineer.castaway.androidApp.view.MediaPlayerFragment
import io.github.lazyengineer.castaway.shared.entity.Episode
import io.github.lazyengineer.castaway.shared.entity.FeedData
import io.github.lazyengineer.castaway.shared.entity.PlaybackPosition
import io.github.lazyengineer.castaway.shared.usecase.GetStoredFeedUseCase
import io.github.lazyengineer.castaway.shared.usecase.StoredEpisodeFlowableUseCase
import io.github.lazyengineer.castawayplayer.MediaServiceClient
import io.github.lazyengineer.castawayplayer.extention.isPlaying
import io.github.lazyengineer.castawayplayer.service.Constants.MEDIA_ROOT_ID
import io.github.lazyengineer.castawayplayer.source.MediaData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel constructor(
  private val mediaServiceClient: MediaServiceClient,
  private val getStoredFeedUseCase: GetStoredFeedUseCase,
  private val storedEpisodeFlowableUseCase: StoredEpisodeFlowableUseCase,
  private val storeAndGetFeedUseCase: StoreAndGetFeedUseCase,
) : ViewModel() {

  private val subscriptionCallback = object : SubscriptionCallback() {
	override fun onChildrenLoaded(
	  parentId: String,
	  children: MutableList<MediaItem>
	) {
	  super.onChildrenLoaded(parentId, children)
	  viewModelScope.launch {
		loadFeed(TEST_URL)
	  }
	  /**
	   * Usually we would load list of episodes here:
	   *
	   * viewModelScope.launch {
	   *  loadEpisodes(episodeIds)
	   * }
	   *
	   */
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

  private val _playing = MutableLiveData<Boolean>()
  val playing: LiveData<Boolean>
	get() = _playing

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
		currentEpisode.value?.let { episode ->
		  _playing.postValue(playingState(episode.id))
		}

		//		feed.value?.let { feedData ->
		//		  _feed.postValue(feed.value?.copy(episodes = feedData.episodes.map { episode ->
		//			  episode.copy(isPlaying = playingState(episode.id))
		//		  }))
		//		}
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

  private fun collect(feedUrl: String) {
	storedEpisodeFlowableUseCase(feedUrl).subscribe(viewModelScope,
	  onEach = {
		Log.d("MainViewModel", "onEach Episode 🍻")
	  },
	  onError = {},
	  onComplete = {}
	)
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
		duration = mediaServiceClient.nowPlaying.value.duration ?: 1
	  )
	)
  }

  fun fetchFeed() {
	viewModelScope.launch {
	  fetchFeedFromUrl(TEST_URL)
	}
  }

  private suspend fun loadFeed(url: String) {
	withContext(Dispatchers.IO) {
	  getStoredFeedUseCase(url).subscribe(
		this,
		onSuccess = {
		  _feed.postValue(it)
		},
		onError = {},
	  )
	}
  }

  private suspend fun fetchFeedFromUrl(url: String) {
	withContext(Dispatchers.IO) {
	  storeAndGetFeedUseCase(url).subscribe(
		this,
		onSuccess = {
		  prepareMediaData(it.episodes)
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

  fun playingState(mediaId: String): Boolean {
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

	const val TEST_URL = "https://feeds.feedburner.com/blogspot/androiddevelopersbackstage"
  }
}