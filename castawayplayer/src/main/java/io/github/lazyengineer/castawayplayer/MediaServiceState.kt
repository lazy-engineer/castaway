package io.github.lazyengineer.castawayplayer

import io.github.lazyengineer.castawayplayer.source.MediaData

data class MediaServiceState(
  val connected: Boolean = false,
  val nowPlaying: MediaData = NOTHING_PLAYING,
  val playbackState: PlaybackState = DEFAULT_PLAYBACK_STATE,
  val networkFailure: Boolean = false,
  val playbackSpeed: Float = 1f
) {

  companion object {

	private val DEFAULT_PLAYBACK_STATE = PlaybackState(
	  isPrepared = false,
	  isPlayEnabled = false,
	  isPlaying = false,
	  currentPlaybackPosition = 0,
	)

	private val NOTHING_PLAYING: MediaData = MediaData(
	  mediaId = "",
	  mediaUri = "",
	  displayTitle = "",
	)

	val Initial = MediaServiceState()
  }
}
