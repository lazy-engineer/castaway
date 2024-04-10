package io.github.lazyengineer.castaway.androidApp.view.nowplaying.screen

import androidx.annotation.FloatRange
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices.PIXEL
import androidx.compose.ui.tooling.preview.Preview
import io.github.lazyengineer.castaway.androidApp.theme.CastawayTheme
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEpisode
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.ObservePlayer
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingPosition
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingState
import io.github.lazyengineer.castaway.domain.resource.ThemeType.MATERIAL

@Composable
fun NowPlayingScreen(
  state: () -> NowPlayingState,
  modifier: Modifier = Modifier,
  @FloatRange(from = 0.0, to = 1.0) expandedPercentage: () -> Float = { 1f },
  event: (NowPlayingEvent) -> Unit
) {
  LaunchedEffect(Unit) {
    event(ObservePlayer)
  }

  Surface(modifier = modifier.fillMaxSize()) {
    when {
      state().loading -> NowPlayingLoadingScreen()
      state().episode != null -> {
        NowPlayingMotionView(
          expandedPercentage = { expandedPercentage() },
          episode = { state().episode!! },
          playing = { state().playing },
          playbackSpeed = { state().playbackSpeed },
          event = remember { event }
        )
      }
    }
  }
}

@Preview
@Preview(device = PIXEL)
@Composable
fun NowPlayingScreenPreview() {
  CastawayTheme(MATERIAL, true) {
    NowPlayingScreen(
      expandedPercentage = { 1f },
      state = {
        NowPlayingState(
          loading = false,
          playing = true,
          buffering = false,
          played = false,
          playbackSpeed = 1.5f,
          episode = NowPlayingEpisode(
            id = "uu1d",
            title = "Awesome Episode 1",
            subTitle = "How to be just awesome!",
            description = "In this episode...",
            audioUrl = "episode.url",
            imageUrl = "image.url",
            author = "Awesom-O",
            playbackPosition = NowPlayingPosition(position = 1800000L, duration = 2160000L),
            episode = 1,
            podcastUrl = "pod.url"
          ),
        )
      }
    ) {}
  }
}
