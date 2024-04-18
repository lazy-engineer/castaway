package io.github.lazyengineer.castaway.androidApp.view.main

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingState
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingViewModel
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.screen.NowPlayingScreen
import io.github.lazyengineer.castaway.androidApp.view.podcast.PodcastScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun Home(
  modifier: Modifier = Modifier,
  nowPlayingViewModel: NowPlayingViewModel = koinViewModel(),
) {
  val nowPlayingState by nowPlayingViewModel.nowPlayingState.collectAsStateWithLifecycle(NowPlayingState.Initial)

  Home(
    nowPlayingState = { nowPlayingState },
    modifier = modifier,
  ) {
    nowPlayingViewModel.nowPlayingState.handleEvent(it)
  }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun Home(
  nowPlayingState: () -> NowPlayingState,
  modifier: Modifier = Modifier,
  event: (NowPlayingEvent) -> Unit,
) {
  val showMiniPlayer by rememberShowMiniPlayerState(nowPlayingState)
  val animatedHeight by animateDpAsState(
    targetValue = if (showMiniPlayer) 60.dp else 0.dp,
    label = "bottomSheetHeight"
  )

  val scaffoldState = rememberBottomSheetScaffoldState()
  var expandedPercentage by rememberSaveable { mutableFloatStateOf(0f) }

  BottomSheetScaffold(
    sheetContent = {
      NowPlayingScreen(
        state = nowPlayingState,
        expandedPercentage = { expandedPercentage },
        event = event
      )
    },
    scaffoldState = scaffoldState,
    sheetPeekHeight = animatedHeight,
  ) {
    with(LocalDensity.current) {
      expandedPercentage = calculateBottomSheetExpandedPercentage(scaffoldState.bottomSheetState.requireOffset().toDp())
    }

    PodcastScreen(modifier = modifier) {

    }
  }
}

@Composable
private fun rememberShowMiniPlayerState(state: () -> NowPlayingState): State<Boolean> {
  return remember {
    derivedStateOf {
      state().episode?.id != null
    }
  }
}

@SuppressWarnings("MagicNumber")
@Composable
private fun calculateBottomSheetExpandedPercentage(
  bottomSheetStateOffset: Dp,
): Float {
  return ((-0.154 * bottomSheetStateOffset.value + 100) / 100).toFloat().coerceIn(
    minimumValue = 0f,
    maximumValue = 1f
  )
}

@Preview
@Composable
fun HomePreview() {
  Home()
}
