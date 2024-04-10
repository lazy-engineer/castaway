package io.github.lazyengineer.castaway.androidApp.view.nowplaying.screen.component

import androidx.compose.foundation.clickable
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.lazyengineer.castaway.androidApp.theme.CastawayTheme
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.EditPlaybackSpeed

@Composable
internal fun PlaybackSpeed(
  playbackSpeed: Float,
  modifier: Modifier = Modifier,
  event: (NowPlayingEvent) -> Unit
) {
  Text(
    text = "${playbackSpeed}x",
    color = CastawayTheme.colors.onBackground,
    modifier = modifier
      .clickable {
        event(EditPlaybackSpeed(2f))
      })
}
