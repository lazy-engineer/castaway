package io.github.lazyengineer.castaway.androidApp.view.nowplaying.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import io.github.lazyengineer.castaway.androidApp.theme.CastawayTheme
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingState
import io.github.lazyengineer.castaway.domain.resource.ThemeType.MATERIAL

@Composable
internal fun NowPlayingLoadingScreen(
  modifier: Modifier = Modifier,
) {
  Column(
	modifier = modifier.background(CastawayTheme.colors.background),
	verticalArrangement = Arrangement.Center,
	horizontalAlignment = Alignment.CenterHorizontally
  ) {
	Text(text = "Loading...", color = CastawayTheme.colors.onBackground, fontSize = 34.sp, fontWeight = FontWeight.Bold)
  }
}

@Preview
@Composable
fun NowPlayingScreenLoadingPreview() {
  CastawayTheme(MATERIAL, true) {
	NowPlayingScreen(state = { NowPlayingState.Initial }) {}
  }
}
