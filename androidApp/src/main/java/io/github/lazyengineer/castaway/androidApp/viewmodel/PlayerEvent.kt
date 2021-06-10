package io.github.lazyengineer.castaway.androidApp.viewmodel

import android.support.v4.media.session.PlaybackStateCompat
import io.github.lazyengineer.castawayplayer.source.MediaData

sealed class PlayerEvent {
  object Subscribe : PlayerEvent()
  object Unsubscribe : PlayerEvent()

  object Rewind : PlayerEvent()
  object FastForward : PlayerEvent()
  object ChangePlaybackSpeed : PlayerEvent()

  data class Connected(val connected: Boolean) : PlayerEvent()
  data class PrepareData(val data: List<MediaData>) : PlayerEvent()
  data class NowPlaying(val mediaData: MediaData) : PlayerEvent()
  data class PlaybackState(val playbackState: PlaybackStateCompat) : PlayerEvent()
  data class PlaybackPosition(val position: Long) : PlayerEvent()
  data class SeekTo(val positionMillis: Long) : PlayerEvent()
  data class PlaybackSpeed(val speed: Float) : PlayerEvent()
  data class PlayPause(val itemId: String) : PlayerEvent()
}
