package io.github.lazyengineer.castaway.androidApp.view.nowplaying

import io.github.lazyengineer.castaway.domain.common.UiEvent
import io.github.lazyengineer.castaway.domain.entity.Episode

sealed class NowPlayingEvent : UiEvent {
  data object Rewind : NowPlayingEvent()
  data object FastForward : NowPlayingEvent()
  data class EditPlaybackSpeed(val speed: Float) : NowPlayingEvent()
  data class SeekTo(val positionMillis: Long) : NowPlayingEvent()
  data class PlayPause(val itemId: String) : NowPlayingEvent()

  data class EpisodeLoaded(val episode: Episode) : NowPlayingEvent()
  data class EpisodeUpdated(val episode: Episode) : NowPlayingEvent()
  data class Playing(val playing: Boolean) : NowPlayingEvent()
  data object ObservePlayer : NowPlayingEvent()
}
