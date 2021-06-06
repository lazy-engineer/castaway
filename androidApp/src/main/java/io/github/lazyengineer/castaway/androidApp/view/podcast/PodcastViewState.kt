package io.github.lazyengineer.castaway.androidApp.view.podcast

import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEpisode

data class PodcastViewState(
  val loading: Boolean = true,
  val title: String = "",
  val imageUrl: String = "",
  val episodes: List<NowPlayingEpisode> = emptyList(),
) {

  companion object {

    val Empty = PodcastViewState()
  }
}