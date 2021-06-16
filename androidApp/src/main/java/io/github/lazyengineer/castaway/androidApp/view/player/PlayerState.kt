package io.github.lazyengineer.castaway.androidApp.view.player

import io.github.lazyengineer.castawayplayer.source.MediaData

data class PlayerState(
  val connected: Boolean = false,
  val prepared: Boolean = false,
  val mediaData: MediaData? = null,
  val playing: Boolean = false,
) {

  companion object {
	val Empty = PlayerState()
  }
}
