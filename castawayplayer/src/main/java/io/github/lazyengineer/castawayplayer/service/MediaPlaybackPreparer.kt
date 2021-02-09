package io.github.lazyengineer.castawayplayer.service

import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.session.PlaybackStateCompat
import com.google.android.exoplayer2.ControlDispatcher
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import io.github.lazyengineer.castawayplayer.service.Constants.PLAYBACK_SPEED
import io.github.lazyengineer.castawayplayer.service.Constants.PLAYBACK_SPEED_CHANGED
import io.github.lazyengineer.castawayplayer.source.MediaData
import io.github.lazyengineer.castawayplayer.source.MediaSource

class MediaPlaybackPreparer(
	private val mediaSource: MediaSource,
	private val playbackSpeedChanged: (playbackSpeed: Float) -> Unit,
	private val mediaItemPrepared: (itemToPlay: MediaData, playWhenReady: Boolean, extras: Bundle?) -> Unit,
) : MediaSessionConnector.PlaybackPreparer {

  override fun getSupportedPrepareActions(): Long =
	PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID or
			PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID

  override fun onPrepare(playWhenReady: Boolean) = Unit

  override fun onPrepareFromMediaId(
	  mediaId: String,
	  playWhenReady: Boolean,
	  extras: Bundle?
  ) {
	mediaSource.whenReady {
	  val itemToPlay = mediaSource.find { item ->
		item.mediaId == mediaId
	  }
	  if (itemToPlay == null) {
		// TODO: Notify caller of the error.
	  } else {
		mediaItemPrepared(itemToPlay, playWhenReady, extras)
	  }
	}
  }

  override fun onPrepareFromSearch(
	  query: String,
	  playWhenReady: Boolean,
	  extras: Bundle?
  ) = Unit

  override fun onPrepareFromUri(
	  uri: Uri,
	  playWhenReady: Boolean,
	  extras: Bundle?
  ) = Unit

  override fun onCommand(
	  player: Player,
	  controlDispatcher: ControlDispatcher,
	  command: String,
	  extras: Bundle?,
	  cb: ResultReceiver?
  ): Boolean =
	when (command) {
		PLAYBACK_SPEED_CHANGED -> {
			extras?.let { playbackSpeedChanged(it.getFloat(PLAYBACK_SPEED)) }
			true
		}
	  else -> false
	}
}
