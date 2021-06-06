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
import androidx.compose.ui.unit.dp
import io.github.lazyengineer.castaway.androidApp.view.PlaybackProgressView

@Composable
fun EpisodeRowView(
  modifier: Modifier = Modifier,
  state: EpisodeRowState = EpisodeRowState.Empty,
  onPlayPause: (Boolean) -> Unit
) {
  Column(modifier = modifier) {
	Row(
	  modifier = Modifier.height(70.dp).fillMaxSize().padding(8.dp),
	  horizontalArrangement = Arrangement.SpaceBetween,
	  verticalAlignment = Alignment.CenterVertically,
	) {
	  Text(state.title, modifier = Modifier.weight(5f))

	  val playPauseImage = when (state.playing) {
		true -> Filled.Pause
		else -> Filled.PlayArrow
	  }

	  Icon(playPauseImage, "play/pause", modifier = Modifier.padding(8.dp).weight(1f).clickable { onPlayPause(true) })
	}

	PlaybackProgressView(modifier = Modifier.fillMaxWidth(), playbackPosition = state.progress, padding = 0.dp)

	Divider()
  }
}
