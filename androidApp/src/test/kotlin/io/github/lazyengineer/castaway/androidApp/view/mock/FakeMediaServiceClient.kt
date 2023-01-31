package io.github.lazyengineer.castaway.androidApp.view.mock

import android.support.v4.media.MediaBrowserCompat.SubscriptionCallback
import io.github.lazyengineer.castawayplayer.MediaServiceClient
import io.github.lazyengineer.castawayplayer.MediaServiceEvent
import io.github.lazyengineer.castawayplayer.MediaServiceEvent.FastForward
import io.github.lazyengineer.castawayplayer.MediaServiceEvent.PlayMediaId
import io.github.lazyengineer.castawayplayer.MediaServiceEvent.RepeatMode
import io.github.lazyengineer.castawayplayer.MediaServiceEvent.Rewind
import io.github.lazyengineer.castawayplayer.MediaServiceEvent.SeekTo
import io.github.lazyengineer.castawayplayer.MediaServiceEvent.Shuffle
import io.github.lazyengineer.castawayplayer.MediaServiceEvent.SkipToNext
import io.github.lazyengineer.castawayplayer.MediaServiceEvent.SkipToPrevious
import io.github.lazyengineer.castawayplayer.MediaServiceEvent.Speed
import io.github.lazyengineer.castawayplayer.MediaServiceState
import io.github.lazyengineer.castawayplayer.config.MediaServiceConfig
import io.github.lazyengineer.castawayplayer.source.MediaData
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class FakeMediaServiceClient(private val config: MediaServiceConfig = MediaServiceConfig()) : MediaServiceClient {

  private val playerState = MutableStateFlow(MediaServiceState.Initial)
  private val coroutineScope = MainScope()
  private var job: Job? = null

  override fun playerState() = playerState.asStateFlow()

  private fun playbackPosition(duration: Long) = (1_000..duration step 1_000)
	.asSequence()
	.asFlow()
	.onEach {
	  delay(1.seconds)
	}

  override suspend fun subscribe(parentId: String, callback: SubscriptionCallback) {
	playerState.update {
	  it.copy(connected = true)
	}
  }

  override fun unsubscribe(parentId: String, callback: SubscriptionCallback) {
	playerState.update {
	  it.copy(connected = false)
	}
  }

  override fun prepare(playlist: List<MediaData>) {
	if (playlist.isNotEmpty()) {
	  playerState.update {
		it.copy(
		  nowPlaying = playlist.first(),
		  playbackState = it.playbackState.copy(isPrepared = true)
		)
	  }
	}
  }

  override fun dispatchMediaServiceEvent(event: MediaServiceEvent) {
	when (event) {
	  FastForward -> {
		playerState.update { state ->
		  state.copy(
			playbackState = state.playbackState.copy(
			  currentPlaybackPosition = state.playbackState.currentPlaybackPosition + config.fastForwardInterval
			)
		  )
		}
	  }
	  is PlayMediaId -> {
		playerState.update { state ->
		  if (state.nowPlaying.mediaId == event.mediaId) {
			state.copy(
			  playbackState = state.playbackState.copy(
				isPlaying = !state.playbackState.isPlaying
			  )
			)
		  } else {
			state.copy(
			  nowPlaying = state.nowPlaying.copy(mediaId = event.mediaId),
			  playbackState = state.playbackState.copy(
				isPlaying = true
			  )
			)
		  }
		}

		startPlayback()
	  }
	  is RepeatMode -> TODO()
	  Rewind -> {
		playerState.update { state ->
		  var rewoundPosition = state.playbackState.currentPlaybackPosition - config.rewindInterval

		  if (rewoundPosition < 0) {
			rewoundPosition = 0
		  }

		  state.copy(
			playbackState = state.playbackState.copy(
			  currentPlaybackPosition = rewoundPosition
			)
		  )
		}
	  }
	  is SeekTo -> {
		playerState.update { state ->
		  state.copy(playbackState = state.playbackState.copy(currentPlaybackPosition = event.position))
		}
	  }
	  is Shuffle -> TODO()
	  SkipToNext -> TODO()
	  SkipToPrevious -> TODO()
	  is Speed -> {
		playerState.update { state ->
		  state.copy(playbackSpeed = event.speed)
		}
	  }
	}
  }

  private fun startPlayback() {
	if (isJobNotRunning(job)) {
	  job = coroutineScope.launch {
		val state = playerState.first()
		if (state.playbackState.isPlaying) {
		  playbackPosition(state.nowPlaying.duration ?: 1_000_000).collect {
			playerState.update { stateToUpdate ->
			  stateToUpdate.copy(playbackState = stateToUpdate.playbackState.copy(currentPlaybackPosition = it))
			}
		  }
		} else {
		  job?.cancel()
		}
	  }
	} else {
	  job?.cancel()
	}
  }

  private fun isJobNotRunning(downloadEventsJob: Job?): Boolean {
	return downloadEventsJob == null || downloadEventsJob.isCancelled || downloadEventsJob.isCompleted
  }
}
