package io.github.lazyengineer.castaway.androidApp.view

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
import io.github.lazyengineer.castaway.androidApp.view.EpisodeRowState.Buffering
import io.github.lazyengineer.castaway.androidApp.view.EpisodeRowState.Downloading
import io.github.lazyengineer.castaway.androidApp.view.EpisodeRowState.Paused
import io.github.lazyengineer.castaway.androidApp.view.EpisodeRowState.Played
import io.github.lazyengineer.castaway.androidApp.view.EpisodeRowState.Playing
import io.github.lazyengineer.castaway.androidApp.view.EpisodeRowState.Unplayed

@Composable
fun EpisodeRowView(
  modifier: Modifier = Modifier,
  state: EpisodeRowState = Unplayed,
  title: String,
  progress: Float,
  onPlayPause: (Boolean) -> Unit
) {
  Column(modifier = modifier) {
	Row(
	  modifier = Modifier.height(70.dp).fillMaxSize().padding(8.dp),
	  horizontalArrangement = Arrangement.SpaceBetween,
	  verticalAlignment = Alignment.CenterVertically,
	) {
	  Text(title, modifier = Modifier.weight(5f))

	  val playPauseImage = when (state) {
		Paused -> Filled.PlayArrow
		Playing -> Filled.Pause
		Played -> Filled.PlayArrow
		Buffering -> Filled.PlayArrow
		Downloading -> Filled.PlayArrow
		Unplayed -> Filled.PlayArrow
	  }

	  Icon(playPauseImage, "play/pause", modifier = Modifier.padding(8.dp).weight(1f).clickable { onPlayPause(true) })
	}

	PlaybackProgressView(modifier = Modifier.fillMaxWidth(), playbackPosition = progress, padding = 0.dp)

	Divider()
  }
}

sealed class EpisodeRowState {
  object Unplayed : EpisodeRowState()
  object Playing : EpisodeRowState()
  object Paused : EpisodeRowState()
  object Buffering : EpisodeRowState()
  object Downloading : EpisodeRowState()
  object Played : EpisodeRowState()
}
