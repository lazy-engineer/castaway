package io.github.lazyengineer.castaway.androidApp.view.podcast

data class EpisodeRowState(
  val playing: Boolean = false,
  val title: String = "",
  val progress: Float = 0f,
  val buffering: Boolean = false,
  val downloading: Boolean = false,
  val played: Boolean = false,
) {

  companion object {

	val Empty = EpisodeRowState()
  }
}
