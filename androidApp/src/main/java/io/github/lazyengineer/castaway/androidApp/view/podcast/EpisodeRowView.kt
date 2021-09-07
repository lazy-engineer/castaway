package io.github.lazyengineer.castaway.androidApp.view.podcast

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.lazyengineer.castaway.androidApp.theme.CastawayTheme
import io.github.lazyengineer.castaway.androidApp.view.PlaybackProgressView
import io.github.lazyengineer.castaway.shared.resource.ThemeType.MATERIAL

@Composable
fun EpisodeRowView(
  modifier: Modifier = Modifier,
  state: EpisodeRowState = EpisodeRowState.Empty,
  onPlayPause: (Boolean) -> Unit
) {
  Column(modifier = modifier) {
	Row(
	  modifier = Modifier
		.height(70.dp)
		.fillMaxSize()
		.padding(8.dp),
	  horizontalArrangement = Arrangement.SpaceBetween,
	  verticalAlignment = Alignment.CenterVertically,
	) {
	  Text(state.title, color = CastawayTheme.colors.onBackground, modifier = Modifier.weight(5f))

	  val playPauseImage = when (state.playing) {
		true -> Filled.Pause
		else -> Filled.PlayArrow
	  }

	  Icon(
		imageVector = playPauseImage,
		contentDescription = "play/pause",
		tint = CastawayTheme.colors.onBackground,
		modifier = Modifier
		  .padding(8.dp)
		  .weight(1f)
		  .clickable { onPlayPause(true) }
		  .semantics {
			stateDescription = playPauseImage.name
		  }
	  )
	}

	PlaybackProgressView(
	  modifier = Modifier
		.fillMaxWidth()
		.padding(16.dp)
		.testTag("playback_progress")
		.semantics {
		  contentDescription = "${state.progress.times(100)}% playback progress"
		},
	  playbackPosition = state.progress,
	  padding = 0.dp
	)

	Divider(color = CastawayTheme.colors.onBackground.copy(alpha = 0.12f))
  }
}

@Preview
@Composable
fun EpisodeRowView_Empty_Preview() {
  CastawayTheme(MATERIAL, false) {
	EpisodeRowView(state = EpisodeRowState.Empty) {}
  }
}

@Preview
@Composable
fun EpisodeRowView_Preview() {
  CastawayTheme(MATERIAL, true) {
	EpisodeRowView(
	  state = EpisodeRowState(
		playing = true,
		title = "Awesome Episode 1",
		progress = .3f,
		buffering = false,
		downloading = false,
		played = false,
	  )
	) {}
  }
}
