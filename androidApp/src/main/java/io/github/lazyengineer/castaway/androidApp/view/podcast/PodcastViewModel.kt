package io.github.lazyengineer.castaway.androidApp.view.podcast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.lazyengineer.castaway.androidApp.player.PlayPauseUseCase
import io.github.lazyengineer.castaway.androidApp.player.PlayerState
import io.github.lazyengineer.castaway.androidApp.player.PlayerStateUseCase
import io.github.lazyengineer.castaway.androidApp.player.PreparePlayerUseCase
import io.github.lazyengineer.castaway.androidApp.player.SubscribeToPlayerUseCase
import io.github.lazyengineer.castaway.androidApp.view.podcast.PodcastEpisode.Companion.toEpisode
import io.github.lazyengineer.castaway.androidApp.view.podcast.PodcastEpisode.Companion.toPodcastEpisode
import io.github.lazyengineer.castaway.androidApp.view.podcast.PodcastEvent.EpisodeRowEvent.PlayPause
import io.github.lazyengineer.castaway.androidApp.view.podcast.PodcastEvent.EpisodeRowEvent.PlaybackPosition
import io.github.lazyengineer.castaway.androidApp.view.podcast.PodcastEvent.EpisodeRowEvent.Playing
import io.github.lazyengineer.castaway.androidApp.view.podcast.PodcastEvent.EpisodeRowEvent.ShowDetails
import io.github.lazyengineer.castaway.androidApp.view.podcast.PodcastEvent.FeedEvent.DetailsShowed
import io.github.lazyengineer.castaway.androidApp.view.podcast.PodcastEvent.FeedEvent.FetchError
import io.github.lazyengineer.castaway.androidApp.view.podcast.PodcastEvent.FeedEvent.Load
import io.github.lazyengineer.castaway.androidApp.view.podcast.PodcastEvent.FeedEvent.Loaded
import io.github.lazyengineer.castaway.domain.common.stateReducerFlow
import io.github.lazyengineer.castaway.domain.entity.common.DataResult.Error
import io.github.lazyengineer.castaway.domain.entity.common.DataResult.Success
import io.github.lazyengineer.castaway.domain.usecase.GetStoredFeedUseCase
import io.github.lazyengineer.castaway.domain.usecase.StoreAndGetFeedUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PodcastViewModel(
  initialState: PodcastViewState = PodcastViewState.Initial,
  playerStateUseCase: PlayerStateUseCase,
  private val getStoredFeedUseCase: GetStoredFeedUseCase,
  private val storeAndGetFeedUseCase: StoreAndGetFeedUseCase,
  private val subscribeToPlayerUseCase: SubscribeToPlayerUseCase,
  private val preparePlayerUseCase: PreparePlayerUseCase,
  private val playPauseUseCase: PlayPauseUseCase,
) : ViewModel() {

  val podcastState = stateReducerFlow(
	initialState = initialState,
	reduceState = ::reduceState,
  )

  private fun reduceState(
	currentState: PodcastViewState,
	event: PodcastEvent
  ): PodcastViewState {
	return when (event) {
	  is Loaded -> {
		val episodes = event.feed.episodes.map { it.toPodcastEpisode() }
		preparePlayer(episodes)

		currentState.copy(
		  loading = false,
		  imageUrl = event.feed.info.imageUrl.orEmpty(),
		  episodes = EpisodesList(items = episodes)
		)
	  }

	  is Load -> {
		if (currentState.episodes.items.isEmpty() || event.forceUpdate) {
		  if (event.forceUpdate) {
			viewModelScope.launch {
			  fetchFeed(event.id)
			}
		  } else {
			viewModelScope.launch {
			  loadFeed(event.id)
			}
		  }

		  currentState.copy(loading = true)
		} else {
		  currentState
		}
	  }

	  is ShowDetails -> currentState.copy(showDetails = event.item)
	  is Playing -> {
		currentState.copy(episodes = EpisodesList(currentState.episodes.items.map { episode ->
		  if (event.itemId == episode.id) {
			episode.copy(buffering = false, playing = !episode.playing)
		  } else {
			episode.copy(playing = false)
		  }
		}))
	  }

	  is PlayPause -> {
		playOrPause(event.itemId)
		currentState.copy(episodes = EpisodesList(currentState.episodes.items.map {
		  if (event.itemId == it.id) {
			it.copy(buffering = true)
		  } else {
			it.copy(buffering = false)
		  }
		}))
	  }

	  is PlaybackPosition -> {
		currentState.copy(episodes = currentState.episodes.copy(
		  items = currentState.episodes.items.map { episode ->
			if (event.itemId == episode.id) {
			  episode.copy(
				playbackPosition = event.positionMillis,
				playbackDuration = event.durationMillis,
				playbackProgress = event.positionMillis.toFloat() / event.durationMillis
			  )
			} else {
			  episode
			}
		  }
		))
	  }

	  DetailsShowed -> {
		currentState.copy(showDetails = null)
	  }

	  is FetchError -> currentState.copy(error = event.error.message, loading = false)
	}
  }

  private val playerState: StateFlow<PlayerState> = playerStateUseCase()
	.stateIn(viewModelScope, SharingStarted.Lazily, PlayerState.Initial)

  init {
	subscribeToPlayer()
	collectPlayerState()
  }

  private fun subscribeToPlayer() {
	viewModelScope.launch {
	  subscribeToPlayerUseCase()
	}
  }

  private fun collectPlayerState() {
	viewModelScope.launch {
	  playerState.collect { state ->
		if (state.prepared) {
		  state.mediaData?.mediaId?.let { mediaId ->
			val episode = podcastState.value.episodes.items.firstOrNull { it.id == mediaId }

			if (state.playing != episode?.playing) {
			  podcastState.handleEvent(Playing(mediaId))
			}

			if (state.mediaData.playbackPosition != episode?.playbackPosition) {
			  podcastState.handleEvent(
				PlaybackPosition(
				  itemId = mediaId,
				  positionMillis = state.mediaData.playbackPosition,
				  durationMillis = state.mediaData.duration ?: 0
				)
			  )
			}
		  }
		} else {
		  preparePlayer(podcastState.value.episodes.items)
		}
	  }
	}
  }

  private fun playOrPause(itemId: String) {
	viewModelScope.launch {
	  playPauseUseCase(itemId)
	}
  }

  private fun preparePlayer(episodes: List<PodcastEpisode>) {
	  preparePlayerUseCase(episodes.map { it.toEpisode() })
  }

  private suspend fun loadFeed(url: String) {
	when (val result = getStoredFeedUseCase(url)) {
	  is Error -> {
		fetchFeed(url)
	  }

	  is Success -> {
		podcastState.handleEvent(Loaded(result.data))
	  }
	}
  }

  private fun fetchFeed(url: String) {
	viewModelScope.launch {
	  fetchFeedFromUrl(url)
	}
  }

  private suspend fun fetchFeedFromUrl(url: String) {
	when (val result = storeAndGetFeedUseCase(url)) {
	  is Success -> {
		podcastState.handleEvent(Loaded(result.data))
	  }

	  is Error -> {
		podcastState.handleEvent(FetchError(result.exception))
	  }
	}
  }

  companion object {

	const val TEST_URL = "https://atp.fm/rss"
  }
}
