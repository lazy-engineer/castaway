package io.github.lazyengineer.castaway.androidApp.player

import io.github.lazyengineer.castawayplayer.MediaServiceClient
import io.github.lazyengineer.castawayplayer.MediaServiceEvent

class FastForwardPlaybackUseCase(
  private val castawayPlayer: MediaServiceClient
) {

  operator fun invoke() {
    castawayPlayer.dispatchMediaServiceEvent(MediaServiceEvent.FastForward)
  }
}
