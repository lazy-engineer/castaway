package io.github.lazyengineer.castaway.androidApp.view.player

data class PlayerState(
  val connected: Boolean = false,
  val playbackPosition: Float = 0f,
) {

  companion object {

	val Empty = PlayerState()
  }
}