package io.github.lazyengineer.castawayplayer.service

import android.util.Log
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player

class PlayerEventListener(
  private val playbackStateChanged: (playbackState: Int) -> Unit,
  private val playWhenReadyChanged: (playWhenReady: Boolean) -> Unit
) : Player.Listener {

  override fun onPlaybackStateChanged(playbackState: Int) {
    super.onPlaybackStateChanged(playbackState)
    playbackStateChanged(playbackState)
  }

  override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
    super.onPlayWhenReadyChanged(playWhenReady, reason)
    playWhenReadyChanged(playWhenReady)
  }

  override fun onPlayerErrorChanged(error: PlaybackException?) {
    super.onPlayerErrorChanged(error)
    error?.let {
      Log.e(TAG, "${it.errorCode}: ${it.message}")
    }
  }

  companion object {

    private const val TAG = "PlayerEventListener"
  }
}
