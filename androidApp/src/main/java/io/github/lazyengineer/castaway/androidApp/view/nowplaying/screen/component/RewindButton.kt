package io.github.lazyengineer.castaway.androidApp.view.nowplaying.screen.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons.Sharp
import androidx.compose.material.icons.sharp.Replay30
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import io.github.lazyengineer.castaway.androidApp.theme.CastawayTheme

@Composable
internal fun RewindButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
  Image(
	imageVector = Sharp.Replay30,
	contentDescription = "replay 30 second",
	contentScale = ContentScale.FillBounds,
	colorFilter = ColorFilter.tint(CastawayTheme.colors.onBackground),
	modifier = modifier.clickable { onClick() }
  )
}
