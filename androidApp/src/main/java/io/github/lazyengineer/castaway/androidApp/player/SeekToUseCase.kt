package io.github.lazyengineer.castaway.androidApp.player

import io.github.lazyengineer.castawayplayer.MediaServiceClient
import io.github.lazyengineer.castawayplayer.MediaServiceEvent

class SeekToUseCase(
  private val castawayPlayer: MediaServiceClient
) {

  operator fun invoke(positionMillis: Long) {
	castawayPlayer.dispatchMediaServiceEvent(MediaServiceEvent.SeekTo(positionMillis))
  }
}
