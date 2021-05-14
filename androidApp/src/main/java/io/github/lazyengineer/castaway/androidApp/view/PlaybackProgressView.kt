package io.github.lazyengineer.castaway.androidApp.view

import androidx.compose.foundation.Canvas
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap.Round
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun PlaybackProgressView(
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