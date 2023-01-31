package io.github.lazyengineer.castaway.androidApp.player

import io.github.lazyengineer.castawayplayer.MediaServiceClient
import io.github.lazyengineer.castawayplayer.MediaServiceEvent

class PlaybackSpeedUseCase constructor(
  private val castawayPlayer: MediaServiceClient
) {

  operator fun invoke(speed: Float) {
	castawayPlayer.dispatchMediaServiceEvent(MediaServiceEvent.Speed(speed))
  }
}
