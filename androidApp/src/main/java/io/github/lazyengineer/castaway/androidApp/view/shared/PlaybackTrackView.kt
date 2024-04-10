package io.github.lazyengineer.castaway.androidApp.view.shared

import androidx.annotation.FloatRange
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Slider
import androidx.compose.material.SliderColors
import androidx.compose.material.SliderDefaults
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.compositeOver
import io.github.lazyengineer.castaway.androidApp.theme.CastawayTheme

@Composable
fun PlaybackSliderView(
  @FloatRange(from = 0.0, to = 1.0) progress: Float,
  onValueChange: (Float) -> Unit,
  modifier: Modifier = Modifier,
  onValueChangeStarted: (() -> Unit)? = null,
  onValueChangeFinished: (() -> Unit)? = null,
) {

  val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }

  LaunchedEffect(interactionSource) {
    interactionSource.interactions.collect { interaction ->
      when (interaction) {

        is DragInteraction.Stop,
        is PressInteraction.Release,
        is PressInteraction.Cancel -> {
          onValueChangeFinished?.invoke()
        }

        is PressInteraction.Press,
        is DragInteraction.Start -> {
          onValueChangeStarted?.invoke()
        }
      }
    }
  }

  //TODO: Implement own version of Slider based on PlaybackProgressView
  Slider(
    value = progress,
    onValueChange = {
      onValueChange(it)
    },
    interactionSource = interactionSource,
    valueRange = 0f..1f,
    modifier = modifier,
    colors = sliderColor()
  )
}

@Composable
private fun sliderColor(): SliderColors {
  val activeTrackColor = CastawayTheme.colors.primary
  val activeTickColor = contentColorFor(activeTrackColor).copy(alpha = SliderDefaults.TickAlpha)
  val disabledActiveTrackColor = CastawayTheme.colors.onSurface.copy(alpha = SliderDefaults.DisabledActiveTrackAlpha)
  val disabledInactiveTrackColor = disabledActiveTrackColor.copy(alpha = SliderDefaults.DisabledInactiveTrackAlpha)

  return SliderDefaults.colors(
    thumbColor = CastawayTheme.colors.primary,
    disabledThumbColor = CastawayTheme.colors.onSurface
      .copy(alpha = ContentAlpha.disabled)
      .compositeOver(CastawayTheme.colors.surface),
    activeTrackColor = activeTrackColor,
    inactiveTrackColor = activeTrackColor.copy(alpha = SliderDefaults.InactiveTrackAlpha),
    disabledActiveTrackColor = disabledActiveTrackColor,
    disabledInactiveTrackColor = disabledInactiveTrackColor,
    activeTickColor = activeTickColor,
    inactiveTickColor = activeTrackColor.copy(alpha = SliderDefaults.TickAlpha),
    disabledActiveTickColor = activeTickColor.copy(alpha = SliderDefaults.DisabledTickAlpha),
    disabledInactiveTickColor = disabledInactiveTrackColor.copy(alpha = SliderDefaults.DisabledTickAlpha)
  )
}
