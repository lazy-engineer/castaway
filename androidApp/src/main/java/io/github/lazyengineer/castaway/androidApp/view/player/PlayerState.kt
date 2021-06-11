package io.github.lazyengineer.castaway.androidApp.view.player

import android.support.v4.media.session.PlaybackStateCompat
import io.github.lazyengineer.castawayplayer.source.MediaData

data class PlayerState(
  val connected: Boolean = false,
  val playbackPosition: Long = 0L,
  val mediaData: MediaData? = null,
  val playbackState: PlaybackStateCompat? = null,
  val playing: Boolean = false,
) {

  companion object {

	val Empty = PlayerState()
  }
}