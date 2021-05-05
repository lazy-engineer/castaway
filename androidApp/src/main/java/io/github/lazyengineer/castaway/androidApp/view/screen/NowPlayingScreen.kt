package io.github.lazyengineer.castaway.androidApp.view.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.coil.CoilImage
import io.github.lazyengineer.castaway.androidApp.view.EpisodeState
import io.github.lazyengineer.castaway.androidApp.view.EpisodeState.EpisodeLoading
import io.github.lazyengineer.castaway.androidApp.view.EpisodeState.EpisodePaused
import io.github.lazyengineer.castaway.androidApp.view.EpisodeState.EpisodePlayed
import io.github.lazyengineer.castaway.androidApp.view.EpisodeState.EpisodePlaying
import io.github.lazyengineer.castaway.androidApp.view.PlaybackProgressView
import io.github.lazyengineer.castaway.androidApp.viewmodel.CastawayViewModel
import java.util.concurrent.TimeUnit.HOURS
import java.util.concurrent.TimeUnit.MILLISECONDS
import java.util.concurrent.TimeUnit.MINUTES

@Composable
fun NowPlayingScreen(
  modifier: Modifier = Modifier,
  episodeId: String,
  viewModel: CastawayViewModel,
  state: EpisodeState = EpisodePaused
) {
  val feed = viewModel.feed.collectAsState()
  val episode = viewModel.currentEpisode.collectAsState()
  val episodeTitle = episode.value?.title ?: ""
  val episodeImageUrl = feed.value?.info?.imageUrl ?: ""
  val playbackPosition = viewModel.playbackPosition.collectAsState(0L)
  val playbackDuration = viewModel.playbackDuration.collectAsState()

  val playbackProgress = playbackPosition.value.toFloat() / playbackDuration.value
  val value = remember { mutableStateOf(playbackProgress) }

  Surface(modifier = modifier.fillMaxSize()) {
	Column(horizontalAlignment = Alignment.CenterHorizontally) {
	  Box(modifier = Modifier.padding(top = 48.dp, bottom = 48.dp)) {
		Box(modifier = Modifier.size(300.dp).clip(RoundedCornerShape(25f)).background(MaterialTheme.colors.primary))

		CoilImage(
		  data = episodeImageUrl,
		  contentDescription = "Podcast header image",
		  modifier = Modifier.size(300.dp).padding(5.dp).clip(RoundedCornerShape(25f)),
		  error = {
			Icon(Filled.Mic, "podcast header icon", modifier = Modifier.size(150.dp), tint = Color.Gray)
		  }
		)
	  }

	  Text(episodeTitle, modifier = Modifier.padding(bottom = 16.dp))

	  Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
		IconButton(onClick = {
		  viewModel.replayCurrentItem()
		}) {
		  Icon(Filled.Replay30, "replay 30 second", modifier = Modifier.size(48.dp))
		}
		IconButton(onClick = {
		  viewModel.mediaItemClicked(episodeId)
		}, modifier = Modifier.padding(start = 48.dp, end = 48.dp).size(64.dp)) {
		  val playPauseImage = when (state) {
			EpisodePaused -> Filled.PlayCircleFilled
			EpisodePlaying -> Filled.PauseCircleFilled
			EpisodeLoading -> Filled.PlayCircleFilled
			EpisodePlayed -> Filled.PlayCircleFilled
		  }

		  Icon(playPauseImage, "play/pause", modifier = Modifier.size(64.dp))
		}
		IconButton(onClick = {
		  viewModel.forwardCurrentItem()
		}) {
		  Icon(Filled.Forward30, "fast forward 30 second", modifier = Modifier.size(48.dp))
		}
	  }

	  Column(modifier = Modifier.fillMaxWidth().padding(top = 64.dp)) {
		Row(modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
		  Text(playbackPosition.value.millisToTxt())
		  Text(playbackDuration.value.millisToTxt())
		}

		PlaybackProgressView(modifier = Modifier.fillMaxWidth(), value.value, { value.value = it })
	  }
	}
  }
}

fun Long.millisToTxt() = String.format(
  "%02d:%02d:%02d",
  MILLISECONDS.toHours(this),
  MILLISECONDS.toMinutes(this) - HOURS.toMinutes(MILLISECONDS.toHours(this)),
  MILLISECONDS.toSeconds(this) - MINUTES.toSeconds(MILLISECONDS.toMinutes(this))
)
