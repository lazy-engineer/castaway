package io.github.lazyengineer.castaway.androidApp.view.nowplaying.screen.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.PauseCircleFilled
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import io.github.lazyengineer.castaway.androidApp.theme.CastawayTheme

@Composable
internal fun PlayButton(playing: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
  val playPauseImage = when (playing) {
	true -> Filled.PauseCircleFilled
	else -> Filled.PlayCircleFilled
  }

  Image(
	imageVector = playPauseImage,
	contentDescription = "play/pause",
	contentScale = ContentScale.Fit,
	colorFilter = ColorFilter.tint(CastawayTheme.colors.onBackground),
	modifier = modifier.clickable { onClick() }
  )
}
