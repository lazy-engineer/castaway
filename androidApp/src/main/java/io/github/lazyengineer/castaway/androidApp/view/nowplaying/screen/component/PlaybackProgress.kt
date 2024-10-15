package io.github.lazyengineer.castaway.androidApp.view.nowplaying.screen.component

import androidx.annotation.FloatRange
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import io.github.lazyengineer.castaway.androidApp.theme.CastawayTheme
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.SeekTo
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingPosition
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.screen.util.millisToTxt
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.screen.util.playbackProgress
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.screen.util.progressToPosition
import io.github.lazyengineer.castaway.androidApp.view.shared.PlaybackSliderView
import io.github.lazyengineer.castaway.androidApp.view.shared.PlaybackTrackView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun PlaybackProgress(
  @FloatRange(from = 0.0, to = 1.0) expandedPercentage: () -> Float,
  playbackPosition: Flow<NowPlayingPosition>,
  modifier: Modifier = Modifier,
  event: (NowPlayingEvent) -> Unit,
  editing: (Boolean) -> Unit,
) {

  var playbackPos by rememberSaveable { mutableLongStateOf(0L) }
  var playbackDuration by rememberSaveable { mutableLongStateOf(0L) }
  var editingPlayback by remember { mutableStateOf(false) }

  LaunchedEffect(Unit) {
    println("Editing: PING!!")
    playbackPosition.collectLatest {
      println("Editing: flow ${playbackPosition.hashCode()}")
      println("Editing: PONG!!")
      if (!editingPlayback) {
        println("Editing: PONG??")
        playbackPos = it.safePosition
        playbackDuration = it.duration
      }
    }
  }

  val sliderExpandedPercentage by animateFloatAsState(
    targetValue = expandedPercentage(),
    label = "playbackProgress"
  )

  if (sliderExpandedPercentage >= 0.75f) {
    Column(modifier = modifier.fillMaxWidth()) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(playbackPos.millisToTxt(), color = CastawayTheme.colors.onBackground)
        Text(playbackDuration.millisToTxt(), color = CastawayTheme.colors.onBackground)
      }

      PlaybackSliderView(
        modifier = Modifier.fillMaxWidth(),
        progress = playbackProgress(
          playbackPosition = playbackPos,
          playbackDuration = playbackDuration
        ),
        onValueChange = {
          if (editingPlayback) {
            playbackPos = it.progressToPosition(playbackDuration)
          }
        },
        onValueChangeStarted = {
          editingPlayback = true
          println("Editing: onValueChangeStarted")
          editing(true)
        },
        onValueChangeFinished = {
          event(SeekTo(playbackPos))
          editingPlayback = false
          println("Editing: onValueChangeFinished")
          editing(false)
        })
    }
  } else {
    PlaybackTrackView(
      modifier = modifier.fillMaxWidth(),
      playbackPosition = playbackProgress(
        playbackPosition = playbackPos,
        playbackDuration = playbackDuration
      ),
    )
  }
}
