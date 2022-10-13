package io.github.lazyengineer.castawayplayer

import android.support.v4.media.session.PlaybackStateCompat
import com.google.android.exoplayer2.C
import io.github.lazyengineer.castawayplayer.source.MediaData

data class MediaServiceState(
  val connected: Boolean = false,
  val nowPlaying: MediaData = NOTHING_PLAYING,
  val playbackState: PlaybackStateCompat = EMPTY_PLAYBACK_STATE,
  val networkFailure: Boolean = false,
  val position: Long = C.TIME_UNSET,
) {

  companion object {

	private val EMPTY_PLAYBACK_STATE: PlaybackStateCompat = PlaybackStateCompat.Builder()
	  .setState(PlaybackStateCompat.STATE_NONE, 0, 0f)
	  .build()

	private val NOTHING_PLAYING: MediaData = MediaData(
	  mediaId = "",
	  mediaUri = "",
	  displayTitle = "",
	)

	val Initial = MediaServiceState()
  }
}
