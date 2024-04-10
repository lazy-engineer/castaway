package io.github.lazyengineer.castaway.androidApp.view.nowplaying.screen.component

import androidx.annotation.FloatRange
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import io.github.lazyengineer.castaway.androidApp.theme.CastawayTheme
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.EditPlaybackPosition
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.EditingPlayback
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.SeekTo
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingPosition
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.screen.util.millisToTxt
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.screen.util.playbackProgress
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.screen.util.progressToPosition
import io.github.lazyengineer.castaway.androidApp.view.shared.PlaybackProgressView
import io.github.lazyengineer.castaway.androidApp.view.shared.PlaybackSliderView

@Composable
internal fun PlaybackProgress(
  @FloatRange(from = 0.0, to = 1.0) expandedPercentage: () -> Float,
  playbackPosition: () -> NowPlayingPosition,
  modifier: Modifier = Modifier,
  event: (NowPlayingEvent) -> Unit
) {

  val sliderExpandedPercentage by animateFloatAsState(
    targetValue = expandedPercentage(),
    label = "playbackProgress"
  )

  val playbackProgress = playbackProgress(
    playbackPosition().safePosition,
    playbackPosition().duration
  )

  if (sliderExpandedPercentage >= 0.75f) {
    Column(modifier = modifier.fillMaxWidth()) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(playbackPosition().safePosition.millisToTxt(), color = CastawayTheme.colors.onBackground)
        Text(playbackPosition().duration.millisToTxt(), color = CastawayTheme.colors.onBackground)
      }

      PlaybackSliderView(
        modifier = Modifier.fillMaxWidth(),
        progress = playbackProgress,
        onValueChange = {
          event(
            EditPlaybackPosition(
              it.progressToPosition(
                playbackPosition().duration
              )
            )
          )
        },
        onValueChangeStarted = {
          event(EditingPlayback(true))
        },
        onValueChangeFinished = {
          event(SeekTo(playbackPosition().safePosition))
          event(EditingPlayback(false))
        })
    }
  } else {
    PlaybackProgressView(
      modifier = modifier.fillMaxWidth(),
      playbackPosition = playbackProgress,
    )
  }
}
