package io.github.lazyengineer.castawayplayer.service

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.Util
import io.github.lazyengineer.castawayplayer.config.MediaServiceConfig
import io.github.lazyengineer.castawayplayer.extention.asMediaItem
import io.github.lazyengineer.castawayplayer.extention.asMediaMetadata
import io.github.lazyengineer.castawayplayer.extention.toMediaSource
import io.github.lazyengineer.castawayplayer.notification.MediaNotificationManager
import io.github.lazyengineer.castawayplayer.notification.PlayerNotificationListener
import io.github.lazyengineer.castawayplayer.provider.Injector
import io.github.lazyengineer.castawayplayer.service.Constants.MEDIA_DESCRIPTION_EXTRAS_START_PLAYBACK_POSITION_MS
import io.github.lazyengineer.castawayplayer.service.Constants.MEDIA_ROOT_ID
import io.github.lazyengineer.castawayplayer.service.Constants.NETWORK_FAILURE
import io.github.lazyengineer.castawayplayer.source.MediaData
import io.github.lazyengineer.castawayplayer.source.MediaSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MediaPlayerService : MediaBrowserServiceCompat() {

  private val serviceJob = SupervisorJob()
  private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

  private lateinit var mediaSource: MediaSource
  private lateinit var mediaSession: MediaSessionCompat
  private lateinit var mediaSessionConnector: MediaSessionConnector
  private lateinit var notificationManager: MediaNotificationManager

  private val dataSourceFactory: DefaultDataSource.Factory by lazy {
	DefaultDataSource.Factory(
	  this,
	  DefaultHttpDataSource.Factory().setUserAgent(Util.getUserAgent(this, MEDIA_USER_AGENT))
	)
  }

  private val config: MediaServiceConfig by lazy {
	Injector.getInstance(applicationContext).provideConfig()
  }

  private val playerListener = PlayerEventListener(onPlaybackStateChanged(), onPlayWhenReadyChanged())
  private val exoPlayer: ExoPlayer by lazy {
	buildExoPlayer()
  }

  override fun onCreate() {
	super.onCreate()
	fetchMediaSource()
	flowMediaSource()
	initMediaSession()
	initMediaSessionConnector()
	initNotificationManager()
  }

  private fun buildExoPlayer(): ExoPlayer {
	val mediaAudioAttributes = AudioAttributes.Builder()
	  .setContentType(C.AUDIO_CONTENT_TYPE_SPEECH)
	  .setUsage(C.USAGE_MEDIA)
	  .build()

	return ExoPlayer.Builder(this)
	  .setSeekForwardIncrementMs(config.fastForwardInterval)
	  .setSeekBackIncrementMs(config.rewindInterval)
	  .build().apply {
		setAudioAttributes(mediaAudioAttributes, true)
		setHandleAudioBecomingNoisy(true)
		addListener(playerListener)
	  }
  }

  private fun fetchMediaSource() {
	mediaSource = Injector.getInstance(applicationContext).provideMediaSource()
	serviceScope.launch {
	  mediaSource.fetch()
	}
  }

  private fun flowMediaSource() {
	serviceScope.launch {
	  mediaSource.flow().collect {
		notifyChildrenChanged(MEDIA_ROOT_ID)
	  }
	}
  }

  private fun initMediaSession() {
	val sessionActivityPendingIntent = packageManager?.getLaunchIntentForPackage(packageName)?.let { sessionIntent ->
	  PendingIntent.getActivity(this, 0, sessionIntent, FLAG_IMMUTABLE)
	}

	mediaSession = MediaSessionCompat(this, SERVICE_TAG).apply {
	  setSessionActivity(sessionActivityPendingIntent)
	  isActive = true
	}

	sessionToken = mediaSession.sessionToken
  }

  private fun initNotificationManager() {
	notificationManager = MediaNotificationManager(
	  context = this,
	  imageLoader = Injector
		.getInstance(application)
		.provideImageLoader(),
	  notificationIconResId = config.notificationIconResId,
	  sessionToken = mediaSession.sessionToken,
	  notificationListener = PlayerNotificationListener(this)
	)
	notificationManager.showNotificationForPlayer(exoPlayer)
  }

  private fun initMediaSessionConnector() {
	mediaSessionConnector = MediaSessionConnector(mediaSession)
	mediaSessionConnector.setPlaybackPreparer(
	  MediaPlaybackPreparer(
		mediaSource,
		onPlaybackSpeedChanged(),
		onMediaItemPrepared()
	  )
	)
	mediaSessionConnector.setQueueNavigator(MediaQueueNavigator(mediaSession))
	mediaSessionConnector.setPlayer(exoPlayer)
  }

  private fun onPlaybackSpeedChanged() = { playbackSpeed: Float ->
	exoPlayer.playbackParameters = PlaybackParameters(playbackSpeed)
  }

  private fun onMediaItemPrepared() = { itemToPlay: MediaData, playWhenReady: Boolean, extras: Bundle? ->
	val playbackStartPositionMs = extras?.getLong(
	  MEDIA_DESCRIPTION_EXTRAS_START_PLAYBACK_POSITION_MS,
	  itemToPlay.playbackPosition ?: C.TIME_UNSET
	) ?: itemToPlay.playbackPosition ?: C.TIME_UNSET

	preparePlaylist(itemToPlay, playWhenReady, playbackStartPositionMs)
  }

  override fun onGetRoot(
	clientPackageName: String,
	clientUid: Int,
	rootHints: Bundle?,
  ) = BrowserRoot(MEDIA_ROOT_ID, null)

  override fun onLoadChildren(
	parentId: String,
	result: Result<MutableList<MediaItem>>
  ) {
	when (parentId) {
	  MEDIA_ROOT_ID -> {
		val resultsSent = sendResultWhenReady(result)
		if (!resultsSent) {
		  result.detach()
		}
	  }
	  else -> result.sendResult(null)
	}
  }

  private fun sendResultWhenReady(result: Result<MutableList<MediaItem>>): Boolean {
	return mediaSource.whenReady { successfullyInitialized ->
	  when {
		successfullyInitialized -> {
		  val children = loadChildren()
		  result.sendResult(children.toMutableList())
		}
		else -> {
		  mediaSession.sendSessionEvent(NETWORK_FAILURE, null)
		  result.sendResult(null)
		}
	  }
	}
  }

  private fun loadChildren(): List<MediaItem> {
	return mediaSource.map { mediaData ->
	  mediaData.asMediaItem()
	}
  }

  private fun onPlaybackStateChanged() = { playbackState: Int ->
	when (playbackState) {
	  Player.STATE_BUFFERING,
	  Player.STATE_READY -> {
		notificationManager.showNotificationForPlayer(exoPlayer)
		storeRecentPlayableMedia()
	  }
	  else -> {
		notificationManager.hideNotification()
	  }
	}
  }

  private fun onPlayWhenReadyChanged() = { playWhenReady: Boolean ->
	allowRemoveNotification(paused = !playWhenReady)
  }

  private fun allowRemoveNotification(paused: Boolean) {
	if (paused) {
	  stopForeground(STOP_FOREGROUND_DETACH)
	}
  }

  private fun preparePlaylist(
	itemToPlay: MediaData?,
	playWhenReady: Boolean,
	playbackStartPositionMs: Long,
  ) {
	val initialWindowIndex = if (itemToPlay == null) 0 else mediaSource.indexOf(itemToPlay)

	exoPlayer.playWhenReady = playWhenReady
	exoPlayer.stop()
	exoPlayer.playbackParameters = PlaybackParameters(config.playbackSpeed)

	val playerMediaSource = mediaSource.map { it.asMediaMetadata() }.toMediaSource(dataSourceFactory)
	exoPlayer.setMediaSource(playerMediaSource)
	exoPlayer.prepare()
	exoPlayer.seekTo(initialWindowIndex, playbackStartPositionMs)
  }

  private fun storeRecentPlayableMedia() {
	serviceScope.launch {
	  mediaSource.saveRecent()
	}
  }

  override fun onTaskRemoved(rootIntent: Intent) {
	storeRecentPlayableMedia()
	super.onTaskRemoved(rootIntent)
	exoPlayer.stop()
  }

  override fun onDestroy() {
	mediaSession.run {
	  isActive = false
	  release()
	}

	serviceJob.cancel()
	exoPlayer.removeListener(playerListener)
	exoPlayer.release()
  }

  private inner class MediaQueueNavigator(
	mediaSession: MediaSessionCompat,
  ) : TimelineQueueNavigator(mediaSession) {

	override fun getMediaDescription(
	  player: Player,
	  windowIndex: Int
	): MediaDescriptionCompat =
	  mediaSource.elementAt(windowIndex).asMediaMetadata().description
  }

  companion object {

	private const val SERVICE_TAG = "MediaPlayerService"
	private const val MEDIA_USER_AGENT = "media_user_agent"
  }
}
