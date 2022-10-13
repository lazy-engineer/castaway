package io.github.lazyengineer.castaway.androidApp.view.player

import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaBrowserCompat.SubscriptionCallback
import io.github.lazyengineer.castaway.androidApp.view.player.PlayerEvent.FastForward
import io.github.lazyengineer.castaway.androidApp.view.player.PlayerEvent.PlayPause
import io.github.lazyengineer.castaway.androidApp.view.player.PlayerEvent.PlaybackSpeed
import io.github.lazyengineer.castaway.androidApp.view.player.PlayerEvent.PrepareData
import io.github.lazyengineer.castaway.androidApp.view.player.PlayerEvent.Rewind
import io.github.lazyengineer.castaway.androidApp.view.player.PlayerEvent.SeekTo
import io.github.lazyengineer.castaway.androidApp.view.player.PlayerEvent.SkipToNext
import io.github.lazyengineer.castaway.androidApp.view.player.PlayerEvent.SkipToPrevious
import io.github.lazyengineer.castawayplayer.MediaServiceClient
import io.github.lazyengineer.castawayplayer.MediaServiceEvent
import io.github.lazyengineer.castawayplayer.extention.isPlaying
import io.github.lazyengineer.castawayplayer.extention.isPrepared
import io.github.lazyengineer.castawayplayer.service.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CastawayPlayer constructor(
  private val mediaServiceClient: MediaServiceClient
) {

  private lateinit var coroutineScope: CoroutineScope
  private val subscriptionCallback = object : SubscriptionCallback() {
	override fun onChildrenLoaded(
	  parentId: String, children: MutableList<MediaItem>
	) {
	  super.onChildrenLoaded(parentId, children)
	}
  }

  private val playerEvents = MutableSharedFlow<PlayerEvent>()
  private val playbackSpeed = MutableStateFlow(1f)

  suspend fun playerState(): StateFlow<PlayerState> = combine(
	mediaServiceClient.playerState(),
	playbackSpeed,
  ) { playerState, playbackSpeed ->
	PlayerState(
	  connected = playerState.connected,
	  prepared = playerState.playbackState.isPrepared &&
			  (playerState.nowPlaying.duration != null && playerState.nowPlaying.duration != -1L),
	  mediaData = if (playerState.nowPlaying.mediaId.isNotEmpty()) {
		playerState.nowPlaying.copy(playbackPosition = playerState.position)
	  } else {
		null
	  },
	  playing = playerState.playbackState.isPlaying,
	  playbackSpeed = playbackSpeed
	)
  }.stateIn(coroutineScope, SharingStarted.Eagerly, PlayerState.Empty)

  fun subscribe(scope: CoroutineScope) {
	coroutineScope = scope

	collectPlayerEvents()
	coroutineScope.launch {
	  mediaServiceClient.subscribe(Constants.MEDIA_ROOT_ID, subscriptionCallback)
	}
  }

  fun unsubscribe() {
	mediaServiceClient.unsubscribe(Constants.MEDIA_ROOT_ID, subscriptionCallback)
	coroutineScope.cancel(message = "CastawayPlayer unsubscribed!")
  }

  suspend fun dispatchPlayerEvent(playerEvent: PlayerEvent) {
	playerEvents.emit(playerEvent)
  }

  private fun collectPlayerEvents() {
	coroutineScope.launch {
	  playerEvents.collect { playerEvent ->
		with(mediaServiceClient) {
		  when (playerEvent) {
			is PrepareData -> prepare(playerEvent.data)
			SkipToNext -> dispatchMediaServiceEvent(MediaServiceEvent.SkipToNext)
			SkipToPrevious -> dispatchMediaServiceEvent(MediaServiceEvent.SkipToPrevious)
			FastForward -> dispatchMediaServiceEvent(MediaServiceEvent.FastForward)
			Rewind -> dispatchMediaServiceEvent(MediaServiceEvent.Rewind)
			is PlayPause -> dispatchMediaServiceEvent(MediaServiceEvent.PlayMediaId(mediaId = playerEvent.itemId))
			is SeekTo -> dispatchMediaServiceEvent(MediaServiceEvent.SeekTo(playerEvent.positionMillis))
			is PlaybackSpeed -> {
			  dispatchMediaServiceEvent(MediaServiceEvent.Speed(playerEvent.speed))
			  playbackSpeed.emit(playerEvent.speed)
			}
		  }
		}
	  }
	}
  }
}
