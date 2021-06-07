package io.github.lazyengineer.castaway.androidApp.view.nowplaying

data class NowPlayingState(
  val loading: Boolean = true,
  val playing: Boolean = false,
  val buffering: Boolean = false,
  val played: Boolean = false,
  val episode: NowPlayingEpisode? = null,
) {

  companion object {

	val Empty = NowPlayingState()
  }
}