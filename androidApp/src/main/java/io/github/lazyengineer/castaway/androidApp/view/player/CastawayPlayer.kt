package io.github.lazyengineer.castaway.androidApp.view.player

import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaBrowserCompat.SubscriptionCallback
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import io.github.lazyengineer.castaway.androidApp.view.player.PlayerEvent.FastForward
import io.github.lazyengineer.castaway.androidApp.view.player.PlayerEvent.PlayPause
import io.github.lazyengineer.castaway.androidApp.view.player.PlayerEvent.PlaybackSpeed
import io.github.lazyengineer.castaway.androidApp.view.player.PlayerEvent.PrepareData
import io.github.lazyengineer.castaway.androidApp.view.player.PlayerEvent.Rewind
import io.github.lazyengineer.castaway.androidApp.view.player.PlayerEvent.SeekTo
import io.github.lazyengineer.castaway.androidApp.view.player.PlayerEvent.SkipToNext
import io.github.lazyengineer.castaway.androidApp.view.player.PlayerEvent.SkipToPrevious
import io.github.lazyengineer.castawayplayer.MediaServiceClient
import io.github.lazyengineer.castawayplayer.extention.isPlaying
import io.github.lazyengineer.castawayplayer.service.Constants
import io.github.lazyengineer.castawayplayer.source.MediaData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine

class CastawayPlayer constructor(
  private val mediaServiceClient: MediaServiceClient
) {

  private val subscriptionCallback = object : SubscriptionCallback() {
	override fun onChildrenLoaded(
	  parentId: String,
	  children: MutableList<MediaItem>
	) {
	  super.onChildrenLoaded(parentId, children)
	  Log.d("CastawayPlayer", "subscriptionCallback")
//	  	  viewModelScope.launch {
//	  		loadFeed(CastawayViewModel.TEST_URL)
//	  	  }
	}
  }

  private val playerEvents = MutableSharedFlow<PlayerEvent>()

  private val playerConnecting = MutableStateFlow(false)
  private val playbackPosition = MutableStateFlow(0L)
  private val playing = MutableStateFlow(false)
  private val nowPlayingState = MutableSharedFlow<MediaData>()
  private val playbackState = MutableSharedFlow<PlaybackStateCompat>()

  val playerState: Flow<PlayerState> = combine(
	playerConnecting,
	playbackPosition,
	nowPlayingState,
	playbackState,
	playing,
  ) { connected, position, nowPlaying, playbackState, playing ->
	PlayerState(
	  connected = connected,
	  playbackPosition = position,
	  mediaData = nowPlaying,
	  playbackState = playbackState,
	  playing = playing,
	)
  }

  suspend fun subscribe() {
	collectMediaClientEvents()
	collectPlayerEvents()
	mediaServiceClient.subscribe(Constants.MEDIA_ROOT_ID, subscriptionCallback)
  }

  fun unsubscribe() {
	mediaServiceClient.unsubscribe(Constants.MEDIA_ROOT_ID, subscriptionCallback)
  }

  suspend fun dispatchPlayerEvent(playerEvent: PlayerEvent) {
	playerEvents.emit(playerEvent)
  }

  private suspend fun collectPlayerEvents() {
	playerEvents.collect { playerEvent ->
	  when (playerEvent) {
		is PrepareData -> prepareMediaData(playerEvent.data)
		SkipToNext -> skipToNext()
		SkipToPrevious -> skipToPrevious()
		FastForward -> forwardCurrentItem()
		Rewind -> replayCurrentItem()
		is PlayPause -> playPause(playerEvent.itemId)
		is SeekTo -> seekTo(playerEvent.positionMillis)
		is PlaybackSpeed -> playbackSpeed(playerEvent.speed)
	  }
	}
  }

  private suspend fun collectMediaClientEvents() {
	collectConnectionState()
	collectNowPlaying()
	collectPlaybackState()
	collectPlaybackPositions()
  }

  private suspend fun collectConnectionState() {
	mediaServiceClient.isConnected.collect { connected ->
	  playerConnecting.emit(connected)
	}
  }

  private suspend fun collectNowPlaying() {
	mediaServiceClient.nowPlaying.collect { mediaData ->
	  nowPlayingState.emit(mediaData)
	}
  }

  private suspend fun collectPlaybackState() {
	mediaServiceClient.playbackState.collect {
	  playbackState.emit(it)
	  playing.emit(it.isPlaying)
	}
  }

  private suspend fun collectPlaybackPositions() {
	mediaServiceClient.playbackPosition.collect { position ->
	  playbackPosition.emit(position)
	}
  }

  private fun prepareMediaData(data: List<MediaData>) {
	if (mediaServiceClient.isConnected.value) {
	  mediaServiceClient.prepare(data)
	}
  }

  private fun playPause(clickedItemId: String) {
	if (mediaServiceClient.isConnected.value) {
	  mediaServiceClient.playMediaId(clickedItemId)
	}
  }

  private fun forwardCurrentItem() {
	if (mediaServiceClient.isConnected.value) {
	  mediaServiceClient.fastForward()
	}
  }

  private fun replayCurrentItem() {
	if (mediaServiceClient.isConnected.value) {
	  mediaServiceClient.rewind()
	}
  }

  private fun skipToPrevious() {
	if (mediaServiceClient.isConnected.value) {
	  mediaServiceClient.skipToPrevious()
	}
  }

  private fun skipToNext() {
	if (mediaServiceClient.isConnected.value) {
	  mediaServiceClient.skipToNext()
	}
  }

  private fun seekTo(positionMillis: Long) {
	if (mediaServiceClient.isConnected.value) {
	  mediaServiceClient.seekTo(positionMillis)
	}
  }

  private fun playbackSpeed(speed: Float) {
	if (mediaServiceClient.isConnected.value) {
	  mediaServiceClient.speed(speed)
	}
  }
}
