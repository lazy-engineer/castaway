package io.github.lazyengineer.castaway.androidApp.view.player

import io.github.lazyengineer.castawayplayer.source.MediaData

sealed class PlayerEvent {
  object SkipToNext : PlayerEvent()
  object SkipToPrevious : PlayerEvent()
  object Rewind : PlayerEvent()
  object FastForward : PlayerEvent()

  data class PrepareData(val data: List<MediaData>) : PlayerEvent()
  data class SeekTo(val positionMillis: Long) : PlayerEvent()
  data class PlaybackSpeed(val speed: Float) : PlayerEvent()
  data class PlayPause(val itemId: String) : PlayerEvent()
}
