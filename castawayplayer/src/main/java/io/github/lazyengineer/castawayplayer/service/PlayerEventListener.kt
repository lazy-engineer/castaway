package io.github.lazyengineer.castawayplayer.service

import android.util.Log
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player

class PlayerEventListener(
	private val playerStateChanged: (playWhenReady: Boolean, playbackState: Int) -> Unit
) : Player.EventListener {

	override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
		playerStateChanged(playWhenReady, playbackState)
	}

	override fun onPlayerError(error: ExoPlaybackException) {
		when (error.type) {
			// If the data from MediaSource object could not be loaded the Exoplayer raises
			// a type_source error.
			// An error message is printed to UI via Toast message to inform the user.
			ExoPlaybackException.TYPE_SOURCE -> {
				Log.e(TAG, "TYPE_SOURCE: " + error.sourceException.message)
			}
			// If the error occurs in a render component, Exoplayer raises a type_remote error.
			ExoPlaybackException.TYPE_RENDERER -> {
				Log.e(TAG, "TYPE_RENDERER: " + error.rendererException.message)
			}
			// If occurs an unexpected RuntimeException Exoplayer raises a type_unexpected error.
			ExoPlaybackException.TYPE_UNEXPECTED -> {
				Log.e(TAG, "TYPE_UNEXPECTED: " + error.unexpectedException.message)
			}
			// Occurs when there is a OutOfMemory error.
			ExoPlaybackException.TYPE_OUT_OF_MEMORY -> {
				Log.e(TAG, "TYPE_OUT_OF_MEMORY: " + error.outOfMemoryError.message)
			}
			// If the error occurs in a remote component, Exoplayer raises a type_remote error.
			ExoPlaybackException.TYPE_REMOTE -> {
				Log.e(TAG, "TYPE_REMOTE: " + error.message)
			}
			ExoPlaybackException.TYPE_TIMEOUT -> {
				Log.e(TAG, "TYPE_TIMEOUT: " + error.message)
			}
		}
	}

	companion object {

		private const val TAG = "PlayerEventListener"
	}
}