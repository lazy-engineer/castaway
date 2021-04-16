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
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Forward30
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Replay30
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.coil.CoilImage
import io.github.lazyengineer.castaway.androidApp.viewmodel.CastawayViewModel

@Composable
fun NowPlayingScreen(modifier: Modifier = Modifier, viewModel: CastawayViewModel, episodeId: String) {

  val feed = viewModel.feed.collectAsState()
  val currentEpisode = viewModel.currentEpisode.collectAsState()

  Surface(modifier = modifier.fillMaxSize()) {
	Column(horizontalAlignment = Alignment.CenterHorizontally) {
	  Box(modifier = Modifier.padding(top = 48.dp, bottom = 48.dp)) {
		Box(modifier = Modifier.size(300.dp).clip(RoundedCornerShape(25f)).background(MaterialTheme.colors.primary))

		CoilImage(
		  data = feed.value?.info?.imageUrl ?: "",
		  contentDescription = "Podcast header image",
		  modifier = Modifier.size(300.dp).padding(5.dp).clip(RoundedCornerShape(25f)),
		  error = {
			Icon(Filled.Mic, "podcast header icon", modifier = Modifier.size(150.dp), tint = Color.Gray)
		  }
		)
	  }

	  Text(currentEpisode.value?.title ?: "Episode title", modifier = Modifier.padding(bottom = 16.dp))

	  Row(modifier= Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
		IconButton(onClick = {}) {
		  Icon(Filled.Replay30, "replay 30 second", modifier = Modifier.size(48.dp))
		}
		IconButton(onClick = {}, modifier = Modifier.padding(start = 48.dp, end = 48.dp).size(64.dp)) {
		  Icon(Filled.PlayCircleFilled, "play/pause", modifier = Modifier.size(64.dp))
		}
		IconButton(onClick = {}) {
		  Icon(Filled.Forward30, "fast forward 30 second", modifier = Modifier.size(48.dp))
		}
	  }

	  Slider(value = .5f, onValueChange = {}, modifier = Modifier.fillMaxWidth().padding(top = 64.dp, start = 16.dp, end = 16.dp).clip(RoundedCornerShape(25)))
	}
  }
}