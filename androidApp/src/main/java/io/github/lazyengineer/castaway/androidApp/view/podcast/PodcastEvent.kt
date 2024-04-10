package io.github.lazyengineer.castaway.androidApp.view.podcast

import io.github.lazyengineer.castaway.domain.common.UiEvent
import io.github.lazyengineer.castaway.domain.entity.FeedData

sealed class PodcastEvent : UiEvent {

  sealed class EpisodeRowEvent : PodcastEvent() {
    data class ShowDetails(val item: PodcastEpisode) : EpisodeRowEvent()
    data class PlayPause(val itemId: String) : EpisodeRowEvent()
    data class Playing(val itemId: String) : EpisodeRowEvent()
    data class PlaybackPosition(
      val itemId: String,
      val positionMillis: Long,
      val durationMillis: Long
    ) : EpisodeRowEvent()
  }

  sealed class ContextMenuEvent : PodcastEvent()

  sealed class FeedEvent : PodcastEvent() {
    data class Loaded(val feed: FeedData) : FeedEvent()
    data class FetchError(val error: Exception) : FeedEvent()
    data class Load(val id: String, val forceUpdate: Boolean = false) : FeedEvent()
    data object DetailsShowed : FeedEvent()
  }
}
