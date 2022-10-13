package io.github.lazyengineer.castaway.androidApp.viewmodel

import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEpisode
import io.github.lazyengineer.castaway.domain.common.UiEvent

sealed class EpisodeRowEvent : UiEvent {
  data class PlayPause(val itemId: String) : EpisodeRowEvent()
  data class Click(val item: NowPlayingEpisode) : EpisodeRowEvent()
}

sealed class NowPlayingEvent : UiEvent {
  object Rewind : NowPlayingEvent()
  object FastForward : NowPlayingEvent()
  object ChangePlaybackSpeed : NowPlayingEvent()
  data class SeekTo(val positionMillis: Long) : NowPlayingEvent()
  data class EditPlaybackPosition(val position: Long) : NowPlayingEvent()
  data class EditingPlayback(val editing: Boolean) : NowPlayingEvent()
  data class PlayPause(val itemId: String) : NowPlayingEvent()
}
