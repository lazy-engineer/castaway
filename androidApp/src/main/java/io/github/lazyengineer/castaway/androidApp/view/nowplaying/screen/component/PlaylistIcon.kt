package io.github.lazyengineer.castaway.androidApp.view.nowplaying.screen.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import io.github.lazyengineer.castaway.androidApp.theme.CastawayTheme

@Composable
internal fun PlaylistIcon(
  modifier: Modifier = Modifier,
  onClick: () -> Unit,
) {
  Image(
	imageVector = Filled.PlaylistPlay,
	contentDescription = "List",
	contentScale = ContentScale.Fit,
	colorFilter = ColorFilter.tint(CastawayTheme.colors.onBackground),
	modifier = modifier.clickable(onClick = onClick)
  )
}
