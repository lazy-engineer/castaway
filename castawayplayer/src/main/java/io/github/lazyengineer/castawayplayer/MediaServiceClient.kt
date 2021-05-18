package io.github.lazyengineer.castawayplayer

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v4.media.session.PlaybackStateCompat.RepeatMode
import android.util.Log
import com.google.android.exoplayer2.C
import io.github.lazyengineer.castawayplayer.config.MediaServiceConfig
import io.github.lazyengineer.castawayplayer.extention.asMediaData
import io.github.lazyengineer.castawayplayer.extention.currentPlaybackPosition
import io.github.lazyengineer.castawayplayer.extention.id
import io.github.lazyengineer.castawayplayer.extention.isPlayEnabled
import io.github.lazyengineer.castawayplayer.extention.isPlaying
import io.github.lazyengineer.castawayplayer.extention.isPrepared
import io.github.lazyengineer.castawayplayer.provider.Injector
import io.github.lazyengineer.castawayplayer.service.Constants.NETWORK_FAILURE
import io.github.lazyengineer.castawayplayer.service.Constants.PLAYBACK_SPEED
import io.github.lazyengineer.castawayplayer.service.Constants.PLAYBACK_SPEED_CHANGED
import io.github.lazyengineer.castawayplayer.source.MediaData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MediaServiceClient private constructor(
  context: Context,
  serviceComponent: ComponentName,
  private val config: MediaServiceConfig
) {

  val rootMediaId: String get() = mediaBrowser.root

  private val _isConnected = MutableStateFlow(false)
  private val _nowPlaying = MutableStateFlow(NOTHING_PLAYING)
  private val _playbackState = MutableStateFlow(EMPTY_PLAYBACK_STATE)
  private val _networkFailure = MutableStateFlow(false)
  private val _playbackPosition = MutableStateFlow(C.TIME_UNSET)
  val isConnected: StateFlow<Boolean> = _isConnected
  val nowPlaying: StateFlow<MediaData> = _nowPlaying
  val playbackState: StateFlow<PlaybackStateCompat> = _playbackState
  val networkFailure: StateFlow<Boolean> = _networkFailure
  val playbackPosition: StateFlow<Long> = _playbackPosition

  private val mediaDataSource = Injector.getInstance(context, config).provideMediaSource()
  private val mediaBrowserConnectionCallback = MediaBrowserConnectionCallback(context)
  private lateinit var mediaController: MediaControllerCompat
  private val transportControls: MediaControllerCompat.TransportControls
	get() = mediaController.transportControls

  private val mediaBrowser = MediaBrowserCompat(
	context,
	serviceComponent,
	mediaBrowserConnectionCallback, null,
  ).apply { connect() }

  suspend fun subscribe(
	parentId: String,
	callback: MediaBrowserCompat.SubscriptionCallback
  ) {
	mediaBrowser.subscribe(parentId, callback)
	updatePlaybackPosition()
  }

  fun unsubscribe(
	parentId: String,
	callback: MediaBrowserCompat.SubscriptionCallback
  ) {
	mediaBrowser.unsubscribe(parentId, callback)
  }

  fun prepare(playlist: List<MediaData>) {
	mediaDataSource.prepare(playlist)
  }

  fun prepare(block: () -> List<MediaData>) {
	mediaDataSource.prepare(block)
  }

  fun playMediaId(
	mediaId: String,
	pauseAllowed: Boolean = true
  ) {
	val isPrepared = playbackState.value.isPrepared
	if (isPrepared && mediaId == nowPlaying.value.mediaId) {
	  playbackState.value.let { playbackState ->
		when {
		  playbackState.isPlaying -> if (pauseAllowed) transportControls.pause() else Unit
		  playbackState.isPlayEnabled -> transportControls.play()
		  else -> Log.w(
			MediaServiceClient::class.java.simpleName,
			"Playable item clicked but neither play nor pause are enabled! (mediaId=$mediaId)"
		  )
		}
	  }
	} else {
	  transportControls.playFromMediaId(mediaId, null)
	}
  }

  fun seekTo(position: Long) {
	transportControls.seekTo(position)
  }

  fun fastForward() {
	val currentPosition = playbackState.value.currentPlaybackPosition
	transportControls.seekTo(currentPosition + config.fastForwardInterval)
  }

  fun rewind() {
	val currentPosition = playbackState.value.currentPlaybackPosition
	transportControls.seekTo(currentPosition - config.rewindInterval)
  }

  fun skipToNext() {
	transportControls.skipToNext()
  }

  fun skipToPrevious() {
	transportControls.skipToPrevious()
  }

  fun speed(speed: Float) {
	val playbackSpeedParams = Bundle().apply {
	  putFloat(PLAYBACK_SPEED, speed)
	}
	sendCommand(PLAYBACK_SPEED_CHANGED, playbackSpeedParams) { _, _ -> }
  }

  fun shuffle(shuffle: Boolean) {
	val shuffleMode = if (shuffle) PlaybackStateCompat.SHUFFLE_MODE_NONE else PlaybackStateCompat.SHUFFLE_MODE_ALL
	transportControls.setShuffleMode(shuffleMode)
  }

  fun repeatMode(@RepeatMode repeat: Int) {
	transportControls.setRepeatMode(repeat)
  }

  private fun sendCommand(
	command: String,
	parameters: Bundle?,
	resultCallback: ((Int, Bundle?) -> Unit)
  ) = if (mediaBrowser.isConnected) {
	mediaController.sendCommand(command, parameters, object : ResultReceiver(Handler()) {
	  override fun onReceiveResult(
		resultCode: Int,
		resultData: Bundle?
	  ) {
		resultCallback(resultCode, resultData)
	  }
	})
	true
  } else {
	false
  }

  private suspend fun updatePlaybackPosition() {
	while (true) {
	  val currentPosition = playbackState.value.currentPlaybackPosition
	  if (playbackPosition.value != currentPosition) {
		_playbackPosition.value = currentPosition
	  }
	  delay(config.positionUpdateIntervalMillis)
	}
  }

  private inner class MediaBrowserConnectionCallback(private val context: Context) :
	MediaBrowserCompat.ConnectionCallback() {

	override fun onConnected() {
	  mediaController = MediaControllerCompat(context, mediaBrowser.sessionToken).apply {
		registerCallback(MediaControllerCallback())
	  }

	  _isConnected.value = true
	}

	override fun onConnectionSuspended() {
	  _isConnected.value = false
	}

	override fun onConnectionFailed() {
	  _isConnected.value = false
	}
  }

  private inner class MediaControllerCallback : MediaControllerCompat.Callback() {

	override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
	  _playbackState.value = state ?: EMPTY_PLAYBACK_STATE
	}

	override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
	  // When ExoPlayer stops we will receive a callback with "empty" metadata. This is a
	  // metadata object which has been instantiated with default values. The default value
	  // for media ID is null so we assume that if this value is null we are not playing
	  // anything.
	  _nowPlaying.value = if (metadata?.id == null) {
		NOTHING_PLAYING
	  } else {
		metadata.asMediaData()
	  }
	}

	override fun onQueueChanged(queue: MutableList<MediaSessionCompat.QueueItem>?) = Unit

	override fun onSessionEvent(
	  event: String?,
	  extras: Bundle?
	) {
	  super.onSessionEvent(event, extras)
	  when (event) {
		NETWORK_FAILURE -> _networkFailure.value = true
	  }
	}

	override fun onSessionDestroyed() {
	  mediaBrowserConnectionCallback.onConnectionSuspended()
	}
  }

  companion object {

	@Suppress("PropertyName")
	private val EMPTY_PLAYBACK_STATE: PlaybackStateCompat = PlaybackStateCompat.Builder()
	  .setState(PlaybackStateCompat.STATE_NONE, 0, 0f)
	  .build()

	@Suppress("PropertyName")
	private val NOTHING_PLAYING: MediaData = MediaData(
	  mediaId = "",
	  mediaUri = "",
	  displayTitle = "",
	)

	@Volatile
	private var instance: MediaServiceClient? = null

	fun getInstance(
	  context: Context,
	  serviceComponent: ComponentName,
	  config: MediaServiceConfig = MediaServiceConfig()
	) = instance ?: synchronized(this) {
	  instance ?: MediaServiceClient(context, serviceComponent, config).also { instance = it }
	}
  }
}
