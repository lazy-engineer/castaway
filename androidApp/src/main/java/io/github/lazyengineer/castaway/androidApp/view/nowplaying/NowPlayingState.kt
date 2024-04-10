package io.github.lazyengineer.castaway.androidApp.view.nowplaying

data class NowPlayingState(
  val loading: Boolean = true,
  val playing: Boolean = false,
  val buffering: Boolean = false,
  val editing: Boolean = false,
  val played: Boolean = false,
  val playbackSpeed: Float = 1f,
  val playbackProgress: Float = 0f,
  val episode: NowPlayingEpisode? = null,
) {

  companion object {

    val Initial = NowPlayingState()
  }
}
