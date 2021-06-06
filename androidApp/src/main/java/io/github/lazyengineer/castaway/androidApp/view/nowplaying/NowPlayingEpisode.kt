package io.github.lazyengineer.castaway.androidApp.view.nowplaying

data class NowPlayingEpisode(
  val id: String,
  val title: String,
  val subTitle: String?,
  val audioUrl: String,
  val imageUrl: String?,
  val author: String?,
  val playbackPosition: Long = 0,
  val playbackDuration: Long = 1,
  val playbackSpeed: Float = 1f,
  val playing: Boolean = false,
)