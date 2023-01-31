package io.github.lazyengineer.castaway.androidApp.view.nowplaying.screen.component

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.lazyengineer.castaway.androidApp.theme.CastawayTheme

@Composable
internal fun EpisodeTitle(
  episodeTitle: String,
  modifier: Modifier = Modifier
) {
  Text(
	text = episodeTitle,
	color = CastawayTheme.colors.onBackground,
	modifier = modifier
  )
}
