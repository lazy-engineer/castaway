package io.github.lazyengineer.castaway.androidApp.view.podcast

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.lazyengineer.castaway.androidApp.theme.CastawayTheme
import io.github.lazyengineer.castaway.androidApp.view.shared.PlaybackProgressView
import io.github.lazyengineer.castaway.domain.resource.ThemeType.MATERIAL

@Composable
fun EpisodeRowView(
  onPlayPause: () -> Unit,
  modifier: Modifier = Modifier,
  state: EpisodeRowState = EpisodeRowState.Empty,
  onClick: () -> Unit
) {
  EpisodeRowView(
	modifier = modifier,
	playing = state.playing,
	title = state.title,
	progress = state.progress,
	onPlayPause = onPlayPause,
	onClick = onClick
  )
}

@Composable
fun EpisodeRowView(
  onPlayPause: () -> Unit,
  modifier: Modifier = Modifier,
  playing: Boolean = false,
  title: String = "",
  progress: Float = 0f,
  onClick: () -> Unit,
) {
  Column(modifier = modifier) {
	Row(
	  modifier = Modifier
		.padding(16.dp)
		.clickable(onClick = onClick),
	  horizontalArrangement = Arrangement.SpaceBetween,
	  verticalAlignment = Alignment.CenterVertically,
	) {
	  Text(
		text = title,
		color = CastawayTheme.colors.onBackground,
		modifier = Modifier.weight(5f)
	  )

	  val playPauseImage = when (playing) {
		true -> Filled.Pause
		else -> Filled.PlayArrow
	  }

	  Surface(
		color = CastawayTheme.colors.background,
		modifier = Modifier
		  .weight(1f)
		  .padding(8.dp)
	  ) {
		Icon(
		  imageVector = playPauseImage,
		  contentDescription = "play/pause",
		  tint = CastawayTheme.colors.onBackground,
		  modifier = Modifier
			.clip(CircleShape)
			.clickable(onClick = onPlayPause)
			.semantics {
			  stateDescription = playPauseImage.name
			}
		)
	  }
	}

	PlaybackProgressView(
	  modifier = Modifier
		.fillMaxWidth()
		.padding(horizontal = 16.dp)
		.padding(bottom = 16.dp)
		.testTag("playback_progress")
		.semantics {
		  contentDescription = "${progress.times(100)}% playback progress"
		},
	  playbackPosition = progress,
	)

	Divider(color = CastawayTheme.colors.onBackground.copy(alpha = 0.12f))
  }
}

@Preview
@Composable
fun EpisodeRowViewEmptyPreview() {
  CastawayTheme(MATERIAL, false) {
	EpisodeRowView(state = EpisodeRowState.Empty, onPlayPause = {}) {}
  }
}

@Preview
@Composable
fun EpisodeRowViewPreview() {
  CastawayTheme(MATERIAL, true) {
	EpisodeRowView(
	  state = EpisodeRowState(
		playing = true,
		title = "Awesome Episode 1",
		progress = .3f,
		buffering = false,
		downloading = false,
		played = false,
	  ),
	  onPlayPause = {}
	) {}
  }
}
