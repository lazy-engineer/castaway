package io.github.lazyengineer.castaway.androidApp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.lazyengineer.castaway.androidApp.usecase.StoreAndGetFeedUseCase
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEpisode
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingState
import io.github.lazyengineer.castaway.androidApp.view.player.CastawayPlayer
import io.github.lazyengineer.castaway.androidApp.view.player.PlayerEvent
import io.github.lazyengineer.castaway.androidApp.view.player.PlayerEvent.FastForward
import io.github.lazyengineer.castaway.androidApp.view.player.PlayerEvent.PlayPause
import io.github.lazyengineer.castaway.androidApp.view.player.PlayerEvent.PlaybackSpeed
import io.github.lazyengineer.castaway.androidApp.view.player.PlayerEvent.PrepareData
import io.github.lazyengineer.castaway.androidApp.view.player.PlayerEvent.Rewind
import io.github.lazyengineer.castaway.androidApp.view.player.PlayerEvent.SeekTo
import io.github.lazyengineer.castaway.androidApp.view.podcast.PodcastViewState
import io.github.lazyengineer.castaway.androidApp.viewmodel.UiEvent.EpisodeRowEvent
import io.github.lazyengineer.castaway.androidApp.viewmodel.UiEvent.NowPlayingEvent
import io.github.lazyengineer.castaway.shared.entity.Episode
import io.github.lazyengineer.castaway.shared.entity.FeedData
import io.github.lazyengineer.castaway.shared.entity.PlaybackPosition
import io.github.lazyengineer.castaway.shared.usecase.GetStoredFeedUseCase
import io.github.lazyengineer.castaway.shared.usecase.SaveEpisodeUseCase
import io.github.lazyengineer.castawayplayer.source.MediaData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CastawayViewModel constructor(
  private val castawayPlayer: CastawayPlayer,
  private val getStoredFeedUseCase: GetStoredFeedUseCase,
  private val saveEpisodeUseCase: SaveEpisodeUseCase,
  private val storeAndGetFeedUseCase: StoreAndGetFeedUseCase,
) : ViewModel() {

  private val playerConnected = MutableStateFlow(false)
  private val playerPlaying = MutableStateFlow(false)

  private val feedLoading = MutableStateFlow(false)
  private val feedTitle = MutableStateFlow("")
  private val feedImageUrl = MutableStateFlow("")
  private val episodes = MutableStateFlow(emptyMap<String, Episode>())

  private val playbackEditing = MutableStateFlow(false)
  private val uiEvents = MutableSharedFlow<UiEvent>()

  val podcastState: Flow<PodcastViewState> = combine(
	feedLoading,
	feedTitle,
	feedImageUrl,
	episodes,
  ) { loading, title, imageUrl, episodes ->
	PodcastViewState(
	  loading = loading,
	  title = title,
	  imageUrl = imageUrl,
	  episodes = episodes.map { it.value.toPlayingEpisode() },
	)
  }

  private val _nowPlayingState = MutableStateFlow(NowPlayingState.Empty)
  val nowPlayingState = _nowPlayingState.asStateFlow()

  init {
	castawayPlayer.subscribe(viewModelScope)
	collectUiEvents()
	collectPlayerState()
  }

  private fun collectUiEvents() {
	viewModelScope.launch {
	  uiEvents.collect { uiEvent ->
		when (uiEvent) {
		  is EpisodeRowEvent.Click -> episodeClicked(uiEvent.item)
		  is EpisodeRowEvent.PlayPause -> submitPlayerEvent(PlayPause(uiEvent.itemId))

		  NowPlayingEvent.FastForward -> submitPlayerEvent(FastForward)
		  NowPlayingEvent.Rewind -> submitPlayerEvent(Rewind)
		  NowPlayingEvent.ChangePlaybackSpeed -> changePlaybackSpeed()
		  is NowPlayingEvent.EditingPlayback -> editingPlayback(uiEvent.editing)
		  is NowPlayingEvent.EditPlaybackPosition -> editingPlaybackPosition(uiEvent.position)
		  is NowPlayingEvent.SeekTo -> submitPlayerEvent(SeekTo(uiEvent.positionMillis))
		  is NowPlayingEvent.PlayPause -> submitPlayerEvent(PlayPause(uiEvent.itemId))
		}
	  }
	}
  }

  private fun collectPlayerState() {
	viewModelScope.launch {
	  castawayPlayer.playerState.collect { state ->

		if (state.connected && playerConnected.value.not()) playerConnected()
		if (state.mediaData != null && state.prepared) handleMediaData(state.mediaData, state.playbackSpeed)
		if (state.playing != playerPlaying.value) playerPlaying.emit(state.playing)
	  }
	}
  }

  private fun handleMediaData(mediaData: MediaData, playbackSpeed: Float = 1f) {
	val feedEpisode = episodes.value[mediaData.mediaId]

	feedEpisode?.let {
	  val nowPlayingEpisode = NowPlayingEpisode(
		id = it.id,
		title = it.title,
		subTitle = it.subTitle,
		audioUrl = it.audioUrl,
		imageUrl = it.imageUrl,
		author = it.author,
		playbackPosition = mediaData.playbackPosition ?: it.playbackPosition.position,
		playbackDuration = mediaData.duration ?: it.playbackPosition.duration,
		playbackSpeed = playbackSpeed,
	  )

	  val state = nowPlayingEpisode.nowPlayingState(playingState(nowPlayingEpisode.id))

	  updateNowPlayingState(state)
	}
  }

  private fun updateNowPlayingState(state: NowPlayingState) {
	if (playbackEditing.value.not()) emitNowPlayingState(state)
	updateCurrentEpisodePlaybackPosition()
  }

  private fun playerConnected() {
	viewModelScope.launch {
	  playerConnected.emit(true)
	  loadFeed(TEST_URL)
	}
  }

  private fun updateCurrentEpisodePlaybackPosition() {
	nowPlayingState.value.episode?.let { episode ->
	  val updatedEpisode = episodes.value[episode.id]
		?.copy(playbackPosition = PlaybackPosition(position = episode.playbackPosition, duration = episode.playbackDuration))

	  updatedEpisode?.let {
		viewModelScope.launch {
		  val updatedEpisodes = episodes.value.mapValues { map ->
			if (map.key == it.id) it else map.value
		  }

		  episodes.emit(updatedEpisodes)
		  storeEpisode(updatedEpisode)
		}
	  }
	}
  }

  private fun NowPlayingEpisode.nowPlayingState(playing: Boolean): NowPlayingState {
	return nowPlayingState.value.copy(episode = this, loading = false, playing = playing)
  }

  private fun fetchFeed() {
	viewModelScope.launch {
	  Log.d("🎙", "Fetch: $TEST_URL 🌐")
	  fetchFeedFromUrl(TEST_URL)
	}
  }

  private suspend fun loadFeed(url: String) {
	withContext(Dispatchers.IO) {
	  getStoredFeedUseCase(url).subscribe(
		this,
		onSuccess = { feed ->
		  Log.d("🎙", "Local ✅")
		  submitPlayerEvent(PrepareData(feed.episodes.mapToMediaData()))
		  emitFeedData(feed)
		},
		onError = {
		  Log.d("🎙", "There is no stored Feed: $url ❌ $it 👉 💾 Download...")
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
		  Log.d("🎙", "Fetched 💯")
		  submitPlayerEvent(PrepareData(it.episodes.mapToMediaData()))
		  emitFeedData(it)
		},
		onError = {
		  Log.d("🎙", "Error fetching: ❌ $it")
		},
	  )
	}
  }

  private fun emitFeedData(feed: FeedData) {
	viewModelScope.launch {
	  feedLoading.emit(false)
	  feedTitle.emit(feed.info.title)
	  feedImageUrl.emit(feed.info.imageUrl ?: "")
	  episodes.emit(feed.episodes.associateBy { it.id })
	}
  }

  private suspend fun storeEpisode(episode: Episode) {
	withContext(Dispatchers.IO) {
	  saveEpisodeUseCase(episode).subscribe(
		this,
		onSuccess = {},
		onError = {
		  Log.d("🎙", "Error storing: ❌ $it")
		},
	  )
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

  private fun episodeClicked(clickedItem: NowPlayingEpisode) {
	if (!playingState(clickedItem.id)) {
	  submitPlayerEvent(PlayPause(clickedItem.id))
	}
  }

  private fun changePlaybackSpeed() {
	nowPlayingState.value.episode?.let {
	  val playbackSpeed = nextSupportedPlaybackSpeed(it.playbackSpeed)
	  emitNowPlayingState(nowPlayingState.value.copy(episode = it.copy(playbackSpeed = playbackSpeed)))
	  submitPlayerEvent(PlaybackSpeed(playbackSpeed))
	}
  }

  private fun nextSupportedPlaybackSpeed(currentPlaybackSpeed: Float): Float {

	val supportedSpeedRates = listOf(1.0f, 1.5f, 2.0f)
	val currentIndex = supportedSpeedRates.indexOf(currentPlaybackSpeed)

	var newIndex = 0
	if (supportedSpeedRates.size > currentIndex + 1) {
	  newIndex = currentIndex + 1
	}

	return supportedSpeedRates[newIndex]
  }

  private fun playingState(mediaId: String): Boolean {
	val isActive = mediaId == nowPlayingState.value.episode?.id
	val isPlaying = playerPlaying.value
	return when {
	  !isActive -> false
	  isPlaying -> true
	  else -> false
	}
  }

  private fun editingPlayback(editing: Boolean) {
	viewModelScope.launch {
	  playbackEditing.emit(editing)
	}
  }

  private fun editingPlaybackPosition(position: Long) {
	nowPlayingState.value.episode?.let {
	  emitNowPlayingState(nowPlayingState.value.copy(episode = it.copy(playbackPosition = position)))
	}
  }

  private fun emitNowPlayingState(state: NowPlayingState) {
	viewModelScope.launch {
	  _nowPlayingState.emit(state)
	}
  }

  fun submitEvent(uiEvent: UiEvent) {
	viewModelScope.launch {
	  Log.d("⏰", "📱 ${uiEvent::class.java.simpleName}")
	  uiEvents.emit(uiEvent)
	}
  }

  private fun submitPlayerEvent(playerEvent: PlayerEvent) {
	viewModelScope.launch {
	  Log.d("⏰", "🎵 ${playerEvent::class.java.simpleName}")
	  castawayPlayer.dispatchPlayerEvent(playerEvent)
	}
  }

  override fun onCleared() {
	super.onCleared()
	castawayPlayer.unsubscribe()
  }

  private fun Episode.toPlayingEpisode(): NowPlayingEpisode {
	return NowPlayingEpisode(
	  id = id,
	  title = title,
	  subTitle = subTitle,
	  audioUrl = audioUrl,
	  imageUrl = imageUrl,
	  author = author,
	  playbackPosition = playbackPosition.position,
	  playbackDuration = playbackPosition.duration,
	  playing = playingState(id)
	)
  }

  companion object {

	const val TEST_URL = "https://atp.fm/rss"
  }
}
