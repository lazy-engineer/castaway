package io.github.lazyengineer.castaway.androidApp.view

import androidx.annotation.FloatRange
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap.Round
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun PlaybackProgressView(
  modifier: Modifier,
  @FloatRange(from = 0.0, to = 1.0) progress: Float,
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
  BoxWithConstraints(modifier = modifier.height(48.dp)) {
	val maxPx = constraints.maxWidth.toFloat()
	val minPx = 0f

	val center = Modifier.align(Alignment.CenterStart)
	val thumbSize = 20.dp
	val offset = (maxPx - 20) * progress

	val offsetPosition = remember { mutableStateOf(offset) }

	PlaybackTrack(modifier = center.fillMaxWidth(), playbackPosition = progress)
	PlaybackThumb(
	  modifier = center.pointerInput(Unit) {
		detectDragGestures { change, dragAmount ->
		  change.consumeAllChanges()
		  offsetPosition.value = (offsetPosition.value + dragAmount.x).coerceIn(minPx, maxPx)
		}
	  },
	  thumbOffset = offsetPosition.value,
	  thumbSize = thumbSize,
	  interactionSource = interactionSource,
	)
  }
}

@Composable
private fun PlaybackTrack(
  modifier: Modifier,
  playbackPosition: Float,
  trackColor: Color = MaterialTheme.colors.primary.copy(.25f),
  progressColor: Color = MaterialTheme.colors.primary,
  thumbDp: Dp = 16.dp,
  trackStrokeWidth: Dp = 4.dp,
) {
  Canvas(modifier) {
	val start = Offset(thumbDp.toPx(), center.y)
	val end = Offset(size.width - thumbDp.toPx(), center.y)

	drawLine(
	  trackColor,
	  start,
	  end,
	  trackStrokeWidth.toPx(),
	  Round
	)

	val sliderValue = Offset(start.x + (end.x - start.x) * playbackPosition, center.y)

	drawLine(
	  progressColor,
	  start,
	  sliderValue,
	  trackStrokeWidth.toPx(),
	  Round
	)
  }
}

@Composable
private fun PlaybackThumb(
  modifier: Modifier,
  thumbOffset: Float,
  thumbSize: Dp,
  thumbColor: Color = MaterialTheme.colors.primary,
  interactionSource: MutableInteractionSource,
) {
  Box(modifier.offset { IntOffset(thumbOffset.roundToInt(), 0) }) {

	Surface(
	  shape = CircleShape,
	  color = thumbColor,
	  modifier = modifier
		.focusable(interactionSource = interactionSource)
		.indication(
		  interactionSource = interactionSource,
		  indication = rememberRipple(
			bounded = false,
			radius = 24.dp
		  )
		)
	) {
	  Spacer(Modifier.size(thumbSize, thumbSize))
	}
  }
}
