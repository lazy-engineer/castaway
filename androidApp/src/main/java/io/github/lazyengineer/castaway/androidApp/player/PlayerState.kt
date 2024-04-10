package io.github.lazyengineer.castaway.androidApp.player

import io.github.lazyengineer.castawayplayer.source.MediaData

data class PlayerState(
  val connected: Boolean = false,
  val prepared: Boolean = false,
  val mediaData: MediaData? = null,
  val playing: Boolean = false,
  val playbackSpeed: Float = 1f,
) {

  companion object {

    val Initial = PlayerState()
  }
}
