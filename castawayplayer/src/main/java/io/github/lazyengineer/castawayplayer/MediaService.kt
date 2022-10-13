package io.github.lazyengineer.castawayplayer

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v4.media.session.PlaybackStateCompat.RepeatMode
import android.util.Log
import io.github.lazyengineer.castawayplayer.MediaServiceEvent.FastForward
import io.github.lazyengineer.castawayplayer.MediaServiceEvent.PlayMediaId
import io.github.lazyengineer.castawayplayer.MediaServiceEvent.Rewind
import io.github.lazyengineer.castawayplayer.MediaServiceEvent.SeekTo
import io.github.lazyengineer.castawayplayer.MediaServiceEvent.Shuffle
import io.github.lazyengineer.castawayplayer.MediaServiceEvent.SkipToNext
import io.github.lazyengineer.castawayplayer.MediaServiceEvent.SkipToPrevious
import io.github.lazyengineer.castawayplayer.MediaServiceEvent.Speed
import io.github.lazyengineer.castawayplayer.config.MediaServiceConfig
import io.github.lazyengineer.castawayplayer.extention.asMediaData
import io.github.lazyengineer.castawayplayer.extention.currentPlaybackPosition
import io.github.lazyengineer.castawayplayer.extention.id
import io.github.lazyengineer.castawayplayer.extention.isPlayEnabled
import io.github.lazyengineer.castawayplayer.extention.isPlaying
import io.github.lazyengineer.castawayplayer.extention.isPrepared
import io.github.lazyengineer.castawayplayer.provider.Injector
import io.github.lazyengineer.castawayplayer.service.Constants
import io.github.lazyengineer.castawayplayer.source.MediaData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class MediaService private constructor(
  context: Context,
  serviceComponent: ComponentName,
  private val config: MediaServiceConfig
) : MediaServiceClient {

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

  private val isConnected = MutableStateFlow(MediaServiceState.Initial.connected)
  private val nowPlaying = MutableStateFlow(MediaServiceState.Initial.nowPlaying)
  private val playbackState = MutableStateFlow(MediaServiceState.Initial.playbackState)
  private val networkFailure = MutableStateFlow(MediaServiceState.Initial.networkFailure)
  private val playbackPosition = MutableStateFlow(MediaServiceState.Initial.position)

  override suspend fun playerState(): StateFlow<MediaServiceState> = combine(
	isConnected,
	nowPlaying,
	playbackState,
	networkFailure,
	playbackPosition
  ) { connected, nowPlaying, playbackState, networkFailure, position ->
	MediaServiceState(
	  connected,
	  nowPlaying,
	  playbackState,
	  networkFailure,
	  position
	)
  }.stateIn(CoroutineScope(Dispatchers.Main + SupervisorJob()), SharingStarted.Eagerly, MediaServiceState.Initial)

  override suspend fun subscribe(
	parentId: String,
	callback: MediaBrowserCompat.SubscriptionCallback
  ) {
	mediaBrowser.subscribe(parentId, callback)
	updatePlaybackPosition()
  }

  override fun unsubscribe(
	parentId: String,
	callback: MediaBrowserCompat.SubscriptionCallback
  ) {
	mediaBrowser.unsubscribe(parentId, callback)
  }

  override fun prepare(playlist: List<MediaData>) {
	mediaDataSource.prepare(playlist)
  }

  override fun dispatchMediaServiceEvent(event: MediaServiceEvent) {
	when (event) {
	  is PlayMediaId -> playMediaId(event.mediaId, event.pauseAllowed)
	  is Speed -> speed(event.speed)
	  is Shuffle -> shuffle(event.shuffle)
	  is MediaServiceEvent.RepeatMode -> repeatMode(event.repeat)
	  is SeekTo -> seekTo(event.position)
	  Rewind -> rewind()
	  FastForward -> fastForward()
	  SkipToNext -> skipToNext()
	  SkipToPrevious -> skipToPrevious()
	}
  }

  private fun playMediaId(mediaId: String, pauseAllowed: Boolean) {
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

  private fun seekTo(position: Long) {
	transportControls.seekTo(position)
  }

  private fun fastForward() {
	val currentPosition = playbackState.value.currentPlaybackPosition
	transportControls.seekTo(currentPosition + config.fastForwardInterval)
  }

  private fun rewind() {
	val currentPosition = playbackState.value.currentPlaybackPosition
	transportControls.seekTo(currentPosition - config.rewindInterval)
  }

  private fun skipToNext() {
	transportControls.skipToNext()
  }

  private fun skipToPrevious() {
	transportControls.skipToPrevious()
  }

  private fun speed(speed: Float) {
	val playbackSpeedParams = Bundle().apply {
	  putFloat(Constants.PLAYBACK_SPEED, speed)
	}
	sendCommand(Constants.PLAYBACK_SPEED_CHANGED, playbackSpeedParams) { _, _ -> }
  }

  private fun shuffle(shuffle: Boolean) {
	val shuffleMode = if (shuffle) PlaybackStateCompat.SHUFFLE_MODE_NONE else PlaybackStateCompat.SHUFFLE_MODE_ALL
	transportControls.setShuffleMode(shuffleMode)
  }

  private fun repeatMode(@RepeatMode repeat: Int) {
	transportControls.setRepeatMode(repeat)
  }

  private fun sendCommand(
	command: String,
	parameters: Bundle?,
	resultCallback: ((Int, Bundle?) -> Unit)
  ) = if (mediaBrowser.isConnected) {
	mediaController.sendCommand(command, parameters, object : ResultReceiver(Handler(Looper.getMainLooper())) {
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
		playbackPosition.value = currentPosition
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

	  isConnected.value = true
	}

	override fun onConnectionSuspended() {
	  isConnected.value = false
	}

	override fun onConnectionFailed() {
	  isConnected.value = false
	}
  }

  private inner class MediaControllerCallback : MediaControllerCompat.Callback() {

	override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
	  playbackState.value = state ?: MediaServiceState.Initial.playbackState
	}

	override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
	  // When ExoPlayer stops we will receive a callback with "empty" metadata. This is a
	  // metadata object which has been instantiated with default values. The default value
	  // for media ID is null so we assume that if this value is null we are not playing
	  // anything.
	  nowPlaying.value = if (metadata?.id == null) {
		MediaServiceState.Initial.nowPlaying
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
		Constants.NETWORK_FAILURE -> networkFailure.value = true
	  }
	}

	override fun onSessionDestroyed() {
	  mediaBrowserConnectionCallback.onConnectionSuspended()
	}
  }

  companion object {

	@Volatile
	private var instance: MediaServiceClient? = null

	fun getInstance(
	  context: Context,
	  serviceComponent: ComponentName,
	  config: MediaServiceConfig = MediaServiceConfig()
	) = instance ?: synchronized(this) {
	  instance ?: MediaService(context, serviceComponent, config).also { instance = it }
	}
  }
}
