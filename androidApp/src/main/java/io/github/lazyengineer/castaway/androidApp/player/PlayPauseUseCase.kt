package io.github.lazyengineer.castaway.androidApp.player

import io.github.lazyengineer.castawayplayer.MediaServiceClient
import io.github.lazyengineer.castawayplayer.MediaServiceEvent

class PlayPauseUseCase(
  private val castawayPlayer: MediaServiceClient
) {

  operator fun invoke(itemId: String) {
    castawayPlayer.dispatchMediaServiceEvent(MediaServiceEvent.PlayMediaId(mediaId = itemId))
  }
}
