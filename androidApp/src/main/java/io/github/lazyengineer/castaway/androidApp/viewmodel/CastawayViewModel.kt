package io.github.lazyengineer.castaway.androidApp.viewmodel

import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaBrowserCompat.SubscriptionCallback
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.lazyengineer.castaway.androidApp.usecase.StoreAndGetFeedUseCase
import io.github.lazyengineer.castaway.shared.entity.Episode
import io.github.lazyengineer.castaway.shared.entity.FeedData
import io.github.lazyengineer.castaway.shared.usecase.GetStoredFeedUseCase
import io.github.lazyengineer.castaway.shared.usecase.StoredEpisodeFlowableUseCase
import io.github.lazyengineer.castawayplayer.MediaServiceClient
import io.github.lazyengineer.castawayplayer.extention.isPlaying
import io.github.lazyengineer.castawayplayer.service.Constants.MEDIA_ROOT_ID
import io.github.lazyengineer.castawayplayer.source.MediaData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CastawayViewModel constructor(
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
		Log.d("CastawayViewModel", "load Feed")
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

  private val _feed = MutableStateFlow<FeedData?>(null)
  val feed: StateFlow<FeedData?>
	get() = _feed

  private val _currentEpisode = MutableStateFlow<Episode?>(null)
  val currentEpisode: StateFlow<Episode?>
	get() = _currentEpisode

  private val _playing = MutableStateFlow(false)
  val playing: StateFlow<Boolean>
	get() = _playing

  private val _playbackPosition = MutableStateFlow(0L)
  val playbackPosition: StateFlow<Long>
	get() = _playbackPosition

  private val _playbackDuration = MutableStateFlow(1L)
  val playbackDuration: StateFlow<Long>
	get() = _playbackDuration

  private val _playbackSpeed = MutableStateFlow(1f)
  val playbackSpeed: StateFlow<Float>
	get() = _playbackSpeed

  private val _playbackEditing = MutableStateFlow(false)

  init {
	subscribeToMediaService()
	collectConnectionState()
	collectPlaybackState()
	collectPlaybackPositions()
	collectNowPlaying()
  }

  private fun collectConnectionState() {
	viewModelScope.launch {
	  mediaServiceClient.isConnected.collect { connected ->
		if (connected) {
		  loadFeed(TEST_URL)
		}
	  }
	}
  }

  private fun collectPlaybackState() {
	viewModelScope.launch {
	  mediaServiceClient.playbackState.collect {
		currentEpisode.value?.let { episode ->
		  _playing.value = playingState(episode.id)
		}
	  }
	}
  }

  private fun collectPlaybackPositions() {
	viewModelScope.launch {
	  mediaServiceClient.playbackPosition.collect { position ->
		if (_playbackEditing.value.not()) _playbackPosition.value = position
	  }
	}
  }

  private fun collectNowPlaying() {
	viewModelScope.launch {
	  mediaServiceClient.nowPlaying.collect { mediaData ->
		val feedEpisode = feed.value?.episodes?.firstOrNull { episode ->
		  mediaData.mediaId == episode.id
		}

		feedEpisode?.let {
		  _currentEpisode.emit(it)
		  mediaData.duration?.let { duration -> _playbackDuration.value = duration }
		}
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

  private fun fetchFeed() {
	viewModelScope.launch {
	  Log.d("CastawayViewModel", "fetch Feed")
	  fetchFeedFromUrl(TEST_URL)
	}
  }

  private suspend fun loadFeed(url: String) {
	withContext(Dispatchers.IO) {
	  getStoredFeedUseCase(url).subscribe(
		this,
		onSuccess = {
		  prepareMediaData(it.episodes)
		  _feed.value = it
		},
		onError = {
		  fetchFeed()
		},
	  )
	}
  }

  private suspend fun fetchFeedFromUrl(url: String) {
	withContext(Dispatchers.IO) {
	  storeAndGetFeedUseCase(url).subscribe(
		this,
		onSuccess = {
		  Log.d("CastawayViewModel", "fetch onSuccess: ${it.info}")

		  prepareMediaData(it.episodes)
		},
		onError = {
		  Log.d("CastawayViewModel", "fetch onError")
		},
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


  fun changePlaybackSpeed() {
	val supportedSpeedRates = listOf(1.0f, 1.5f, 2.0f)
	val currentIndex = supportedSpeedRates.indexOf(_playbackSpeed.value)

	var newIndex = 0
	if (supportedSpeedRates.size > currentIndex + 1) {
	  newIndex = currentIndex + 1
	}

	_playbackSpeed.value = supportedSpeedRates[newIndex]
	playbackSpeed(_playbackSpeed.value)
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

  fun editingPlayback(editing: Boolean) {
	_playbackEditing.value = editing
  }

  fun editingPlaybackPosition(position: Long) {
	_playbackPosition.value = position
  }

  override fun onCleared() {
	super.onCleared()
	mediaServiceClient.unsubscribe(MEDIA_ROOT_ID, subscriptionCallback)
  }

  companion object {

	const val TEST_URL = "https://atp.fm/rss"
  }
}