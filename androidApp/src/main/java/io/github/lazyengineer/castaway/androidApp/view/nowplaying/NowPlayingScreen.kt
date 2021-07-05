package io.github.lazyengineer.castaway.androidApp.view.nowplaying

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Forward30
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PauseCircleFilled
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Replay30
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.ImageLoadState
import io.github.lazyengineer.castaway.androidApp.view.PlaybackSliderView
import io.github.lazyengineer.castaway.androidApp.view.style.shadow
import io.github.lazyengineer.castaway.androidApp.view.util.rememberFlowWithLifecycle
import io.github.lazyengineer.castaway.androidApp.viewmodel.CastawayViewModel
import io.github.lazyengineer.castaway.androidApp.viewmodel.UiEvent.NowPlayingEvent
import java.util.concurrent.TimeUnit.HOURS
import java.util.concurrent.TimeUnit.MILLISECONDS
import java.util.concurrent.TimeUnit.MINUTES

@Composable
fun NowPlayingScreen(
  modifier: Modifier = Modifier,
  viewModel: CastawayViewModel,
) {
  val viewState by rememberFlowWithLifecycle(viewModel.nowPlayingState).collectAsState(NowPlayingState.Empty)

  NowPlayingScreen(modifier, viewState) {
	viewModel.submitEvent(it)
  }
}

@Composable
internal fun NowPlayingScreen(
  modifier: Modifier = Modifier,
  state: NowPlayingState,
  event: (NowPlayingEvent) -> Unit
) {
  Surface(modifier = modifier.fillMaxSize()) {

	when {
	  state.loading -> NowPlayingLoadingScreen()
	  state.episode != null -> {
		NowPlayingView(modifier, state.episode, state.playing) {
		  event(it)
		}
	  }
	  else -> {

	  }
	}
  }
}

@Composable
internal fun NowPlayingLoadingScreen(
  modifier: Modifier = Modifier,
) {
  Column(
	modifier = modifier,
	verticalArrangement = Arrangement.Center,
	horizontalAlignment = Alignment.CenterHorizontally
  ) {
	Text("Loading...", fontSize = 34.sp, fontWeight = FontWeight.Bold)
  }
}

@Composable
internal fun NowPlayingView(
  modifier: Modifier = Modifier,
  episode: NowPlayingEpisode,
  playing: Boolean,
  event: (NowPlayingEvent) -> Unit
) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
	Box(modifier = Modifier.padding(top = 48.dp, bottom = 48.dp)) {
	  Box(modifier = Modifier
		.size(300.dp)
		.clip(RoundedCornerShape(25f))
		.background(MaterialTheme.colors.primary)
		.shadow(
		  shadowColor = Color.White.toArgb(),
		  paintColor = Color.White.toArgb(),
		  offsetX = 4.dp,
		  offsetY = 4.dp
		)
	  )

	  val painter = rememberCoilPainter(episode.imageUrl)

	  Image(
		painter = painter,
		modifier = Modifier.size(300.dp).padding(5.dp).clip(RoundedCornerShape(25f)),
		contentDescription = "Podcast header image",
	  )

	  when (painter.loadState) {
		is ImageLoadState.Loading -> {
		  CircularProgressIndicator(Modifier.align(Alignment.Center))
		}
		is ImageLoadState.Error -> {
		  Icon(Filled.Mic, "Podcast header icon", modifier = Modifier.size(150.dp), tint = Color.Gray)
		}
	  }
	}

	Text(episode.title, modifier = Modifier.padding(bottom = 16.dp))

	Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
	  IconButton(
		onClick = {
		  event(NowPlayingEvent.Rewind)
		}) {
		Icon(Filled.Replay30, "replay 30 second", modifier = Modifier.size(48.dp))
	  }

	  IconButton(
		modifier = Modifier.padding(start = 48.dp, end = 48.dp).size(64.dp),
		onClick = {
		  event(NowPlayingEvent.PlayPause(episode.id))
		}) {

		val playPauseImage = when (playing) {
		  true -> Filled.PauseCircleFilled
		  else -> Filled.PlayCircleFilled
		}

		Icon(playPauseImage, "play/pause", modifier = Modifier.size(64.dp))
	  }
	  IconButton(onClick = {
		event(NowPlayingEvent.FastForward)
	  }) {
		Icon(Filled.Forward30, "fast forward 30 second", modifier = Modifier.size(48.dp))
	  }
	}

	Column(modifier = Modifier.fillMaxWidth().padding(top = 64.dp)) {
	  Row(modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
		Text(episode.playbackPosition.millisToTxt())
		Text(episode.playbackDuration.millisToTxt())
	  }

	  PlaybackSliderView(
		modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp),
		progress = playbackProgress(
		  episode.playbackPosition,
		  episode.playbackDuration
		),
		onValueChange = {
		  event(
			NowPlayingEvent.EditPlaybackPosition(
			  it.progressToPosition(
				episode.playbackDuration
			  )
			)
		  )
		},
		onValueChangeStarted = {
		  event(NowPlayingEvent.EditingPlayback(true))
		},
		onValueChangeFinished = {
		  event(NowPlayingEvent.EditingPlayback(false))
		  event(NowPlayingEvent.SeekTo(episode.playbackPosition))
		})
	}

	Text(
	  text = "${episode.playbackSpeed}x",
	  modifier = Modifier.align(alignment = Alignment.Start).padding(start = 16.dp).clickable {
		event(NowPlayingEvent.ChangePlaybackSpeed)
	  })
  }
}

private fun playbackProgress(
  playbackPosition: Long,
  playbackDuration: Long
): Float {
  return when {
	playbackDuration <= 0 -> 0f
	else -> playbackPosition / playbackDuration.toFloat()
  }
}

private fun Float.progressToPosition(playbackDuration: Long) = (this * playbackDuration).toLong()

fun Long.millisToTxt() = String.format(
  "%02d:%02d:%02d",
  MILLISECONDS.toHours(this),
  MILLISECONDS.toMinutes(this) - HOURS.toMinutes(MILLISECONDS.toHours(this)),
  MILLISECONDS.toSeconds(this) - MINUTES.toSeconds(MILLISECONDS.toMinutes(this))
)
