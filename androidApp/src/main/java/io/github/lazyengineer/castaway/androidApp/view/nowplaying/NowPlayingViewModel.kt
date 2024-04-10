package io.github.lazyengineer.castaway.androidApp.view.nowplaying

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.lazyengineer.castaway.androidApp.player.FastForwardPlaybackUseCase
import io.github.lazyengineer.castaway.androidApp.player.PlayPauseUseCase
import io.github.lazyengineer.castaway.androidApp.player.PlaybackSpeedUseCase
import io.github.lazyengineer.castaway.androidApp.player.PlayerState
import io.github.lazyengineer.castaway.androidApp.player.PlayerStateUseCase
import io.github.lazyengineer.castaway.androidApp.player.RewindPlaybackUseCase
import io.github.lazyengineer.castaway.androidApp.player.SeekToUseCase
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEpisode.Companion.toEpisode
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEpisode.Companion.toNowPlayingEpisode
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.EditPlaybackPosition
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.EditPlaybackSpeed
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.EditingPlayback
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.EpisodeLoaded
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.EpisodeUpdated
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.FastForward
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.ObservePlayer
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.PlayPause
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.Playing
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.Rewind
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.SeekTo
import io.github.lazyengineer.castaway.domain.common.stateReducerFlow
import io.github.lazyengineer.castaway.domain.entity.Episode
import io.github.lazyengineer.castaway.domain.entity.common.DataResult.Error
import io.github.lazyengineer.castaway.domain.entity.common.DataResult.Success
import io.github.lazyengineer.castaway.domain.usecase.GetStoredEpisodesUseCase
import io.github.lazyengineer.castaway.domain.usecase.SaveEpisodeUseCase
import io.github.lazyengineer.castawayplayer.source.MediaData
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@Suppress("complexity:LongParameterList")
class NowPlayingViewModel(
  playerStateUseCase: PlayerStateUseCase,
  private val saveEpisodeUseCase: SaveEpisodeUseCase,
  private val getStoredEpisodesUseCase: GetStoredEpisodesUseCase,
  private val playPauseUseCase: PlayPauseUseCase,
  private val seekToUseCase: SeekToUseCase,
  private val fastForwardPlaybackUseCase: FastForwardPlaybackUseCase,
  private val rewindPlaybackUseCase: RewindPlaybackUseCase,
  private val playbackSpeedUseCase: PlaybackSpeedUseCase,
) : ViewModel() {

  val nowPlayingState = stateReducerFlow(
    initialState = NowPlayingState.Initial,
    reduceState = ::reduceState,
  )

  @Suppress("CyclomaticComplexMethod")
  private fun reduceState(currentState: NowPlayingState, event: NowPlayingEvent): NowPlayingState {
    return when (event) {
      is ObservePlayer -> {
        collectPlayerState()
        currentState.copy(loading = true)
      }

      is EditPlaybackSpeed -> {
        playbackSpeedUseCase(event.speed)
        currentState.copy(playbackSpeed = event.speed)
      }

      is EditPlaybackPosition -> {
        currentState.episode?.let { episode ->
          val updatedEpisode = episode.copy(
            playbackPosition = episode.playbackPosition.copy(position = event.positionMillis)
          )

          currentState.copy(episode = updatedEpisode)
        } ?: currentState
      }

      is EditingPlayback -> {
        currentState.copy(editing = event.editing)
      }

      is PlayPause -> {
        playOrPause(event.itemId)
        currentState.copy(buffering = true)
      }

      is Playing -> {
        currentState.copy(playing = event.playing)
      }

      is SeekTo -> {
        seekToUseCase(event.positionMillis)
        currentState.copy(buffering = true)
      }

      FastForward -> {
        fastForwardPlaybackUseCase()
        currentState.copy(buffering = true)
      }

      Rewind -> {
        rewindPlaybackUseCase()
        currentState.copy(buffering = true)
      }

      is EpisodeLoaded -> {
        currentState.copy(loading = false, episode = event.episode.toNowPlayingEpisode())
      }

      is EpisodeUpdated -> {
        if (event.episode != nowPlayingState.value.episode?.toEpisode()) {
          storeEpisode(event.episode)
        }

        currentState.copy(buffering = false, episode = event.episode.toNowPlayingEpisode())
      }
    }
  }

  private val playerState: StateFlow<PlayerState> = playerStateUseCase()
    .stateIn(viewModelScope, SharingStarted.Lazily, PlayerState.Initial)

  private fun collectPlayerState() {
    viewModelScope.launch {
      playerState.collectLatest { state ->
        if (state.prepared) {
          if (state.mediaData != null && !nowPlayingState.value.editing) handleMediaData(nowPlayingState.value, state.mediaData, state.playbackSpeed)
          if (state.playing != nowPlayingState.value.playing) nowPlayingState.handleEvent(Playing(state.playing))
        }
      }
    }
  }

  private fun handleMediaData(playingState: NowPlayingState, mediaData: MediaData, playbackSpeed: Float = 1f) {
    if (playingState.episode != null && playingState.episode.id == mediaData.mediaId) {
      val updatedEpisode = playingState.episode.copy(
        playbackPosition = NowPlayingPosition(
          position = mediaData.playbackPosition,
          duration = mediaData.duration ?: playingState.episode.playbackPosition.duration,
        )
      )

      nowPlayingState.handleEvent(EpisodeUpdated(updatedEpisode.toEpisode()))
      nowPlayingState.handleEvent(EditPlaybackSpeed(playbackSpeed))
    } else {
      loadEpisode(mediaData.mediaId)
    }
  }

  private fun playOrPause(itemId: String) {
    viewModelScope.launch {
      playPauseUseCase(itemId)
    }
  }

  private fun loadEpisode(episodeId: String) {
    viewModelScope.launch {
      when (val result = getStoredEpisodesUseCase(listOf(episodeId))) {
        is Error -> Unit

        is Success -> {
          result.data.firstOrNull()?.let { episode ->
            nowPlayingState.handleEvent(EpisodeLoaded(episode))
          }
        }
      }
    }
  }

  private fun storeEpisode(episode: Episode) {
    viewModelScope.launch {
      saveEpisodeUseCase(episode)
    }
  }
}
