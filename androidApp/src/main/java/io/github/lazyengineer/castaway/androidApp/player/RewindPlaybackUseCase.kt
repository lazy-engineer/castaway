package io.github.lazyengineer.castaway.androidApp.player

import io.github.lazyengineer.castawayplayer.MediaServiceClient
import io.github.lazyengineer.castawayplayer.MediaServiceEvent

class RewindPlaybackUseCase(
  private val castawayPlayer: MediaServiceClient
) {

  operator fun invoke() {
    castawayPlayer.dispatchMediaServiceEvent(MediaServiceEvent.Rewind)
  }
}
