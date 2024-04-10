package io.github.lazyengineer.castaway.androidApp.player

import io.github.lazyengineer.castawayplayer.MediaServiceClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlayerStateUseCase(
  private val castawayPlayer: MediaServiceClient
) {

  operator fun invoke(): Flow<PlayerState> = castawayPlayer.playerState().map { mediaServiceState ->
    PlayerState(
      connected = mediaServiceState.connected,
      prepared = mediaServiceState.playbackState.isPrepared &&
        (mediaServiceState.nowPlaying.duration != null && mediaServiceState.nowPlaying.duration != -1L),
      mediaData = if (mediaServiceState.nowPlaying.mediaId.isNotEmpty()) {
        mediaServiceState.nowPlaying.copy(playbackPosition = mediaServiceState.playbackState.currentPlaybackPosition)
      } else {
        null
      },
      playing = mediaServiceState.playbackState.isPlaying,
      playbackSpeed = mediaServiceState.playbackSpeed
    )
  }
}
