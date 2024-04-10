package io.github.lazyengineer.castaway.androidApp.view.podcast

import androidx.compose.runtime.Immutable

data class PodcastViewState(
  val loading: Boolean = true,
  val title: String = "",
  val imageUrl: String = "",
  val error: String? = null,
  val episodes: EpisodesList = EpisodesList(items = emptyList()),
  val showDetails: PodcastEpisode? = null,
) {

  companion object {

    val Initial = PodcastViewState()
  }
}

@Immutable
data class EpisodesList(val items: List<PodcastEpisode>)
