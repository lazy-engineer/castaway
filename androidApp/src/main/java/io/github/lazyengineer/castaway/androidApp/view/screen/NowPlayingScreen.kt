package io.github.lazyengineer.castaway.androidApp.view.screen

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.ImageLoadState.Error
import com.google.accompanist.imageloading.ImageLoadState.Loading
import io.github.lazyengineer.castaway.androidApp.view.PlaybackSliderView
import io.github.lazyengineer.castaway.androidApp.view.screen.NowPlayingState.Playing
import io.github.lazyengineer.castaway.androidApp.viewmodel.CastawayViewModel
import io.github.lazyengineer.castaway.androidApp.viewmodel.UiEvent
import java.util.concurrent.TimeUnit.HOURS
import java.util.concurrent.TimeUnit.MILLISECONDS
import java.util.concurrent.TimeUnit.MINUTES

@Composable
fun NowPlayingScreen(
  modifier: Modifier = Modifier,
  episodeId: String,
  viewModel: CastawayViewModel,
) {
  val nowPlayingState = viewModel.nowPlayingState.collectAsState()

  Surface(modifier = modifier.fillMaxSize()) {
	Column(horizontalAlignment = Alignment.CenterHorizontally) {
	  Box(modifier = Modifier.padding(top = 48.dp, bottom = 48.dp)) {
		Box(modifier = Modifier.size(300.dp).clip(RoundedCornerShape(25f)).background(MaterialTheme.colors.primary))

		val painter = rememberCoilPainter(nowPlayingState.value.episode?.imageUrl)

		Image(
		  painter = painter,
		  modifier = Modifier.size(300.dp).padding(5.dp).clip(RoundedCornerShape(25f)),
		  contentDescription = "Podcast header image",
		)

		when (painter.loadState) {
		  is Loading -> {
			CircularProgressIndicator(Modifier.align(Alignment.Center))
		  }
		  is Error -> {
			Icon(Filled.Mic, "Podcast header icon", modifier = Modifier.size(150.dp), tint = Color.Gray)
		  }
		}
	  }

	  Text(nowPlayingState.value.episode?.title ?: "", modifier = Modifier.padding(bottom = 16.dp))

	  Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
		IconButton(onClick = {
		  viewModel.handleUiEvent(UiEvent.NowPlayingEvent.Rewind)
		}) {
		  Icon(Filled.Replay30, "replay 30 second", modifier = Modifier.size(48.dp))
		}
		IconButton(onClick = {
		  viewModel.handleUiEvent(UiEvent.NowPlayingEvent.MediaItemClicked(nowPlayingState.value.episode?.id ?: episodeId))
		}, modifier = Modifier.padding(start = 48.dp, end = 48.dp).size(64.dp)) {

		  val playPauseImage = when (nowPlayingState.value) {
			is Playing -> Filled.PauseCircleFilled
			else -> Filled.PlayCircleFilled
		  }

		  Icon(playPauseImage, "play/pause", modifier = Modifier.size(64.dp))
		}
		IconButton(onClick = {
		  viewModel.handleUiEvent(UiEvent.NowPlayingEvent.FastForward)
		}) {
		  Icon(Filled.Forward30, "fast forward 30 second", modifier = Modifier.size(48.dp))
		}
	  }

	  Column(modifier = Modifier.fillMaxWidth().padding(top = 64.dp)) {
		Row(modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
		  Text(nowPlayingState.value.episode?.playbackPosition?.millisToTxt() ?: "")
		  Text(nowPlayingState.value.episode?.playbackDuration?.millisToTxt() ?: "")
		}

		PlaybackSliderView(
		  modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp),
		  progress = playbackProgress(
			nowPlayingState.value.episode?.playbackPosition ?: 0L,
			nowPlayingState.value.episode?.playbackDuration ?: 1L
		  ),
		  onValueChange = {
			viewModel.handleUiEvent(
			  UiEvent.NowPlayingEvent.EditingPlaybackPosition(
				it.progressToPosition(
				  nowPlayingState.value.episode?.playbackDuration ?: 1
				)
			  )
			)
		  },
		  onValueChangeStarted = {
			viewModel.handleUiEvent(UiEvent.NowPlayingEvent.EditingPlayback(true))
		  },
		  onValueChangeFinished = {
			viewModel.handleUiEvent(UiEvent.NowPlayingEvent.EditingPlayback(false))
			viewModel.handleUiEvent(UiEvent.NowPlayingEvent.SeekTo(viewModel.nowPlayingState.value.episode?.playbackPosition ?: 0))
		  })
	  }

	  Text(
		text = "${nowPlayingState.value.episode?.playbackSpeed ?: 1f}x",
		modifier = Modifier.align(alignment = Alignment.Start).padding(start = 16.dp).clickable {
		  viewModel.handleUiEvent(UiEvent.NowPlayingEvent.ChangePlaybackSpeed)
		})
	}
  }
}

sealed class NowPlayingState(val episode: NowPlayingEpisode? = null) {
  object Loading : NowPlayingState()
  data class Playing(val playingEpisode: NowPlayingEpisode) : NowPlayingState(playingEpisode)
  data class Paused(val pausedEpisode: NowPlayingEpisode) : NowPlayingState(pausedEpisode)
  object Buffering : NowPlayingState()
  object Played : NowPlayingState()
}

data class NowPlayingEpisode(
  val id: String,
  val title: String,
  val subTitle: String?,
  val audioUrl: String,
  val imageUrl: String?,
  val author: String?,
  val playbackPosition: Long = 0,
  val playbackDuration: Long = 1,
  val playbackSpeed: Float = 1f,
)

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
