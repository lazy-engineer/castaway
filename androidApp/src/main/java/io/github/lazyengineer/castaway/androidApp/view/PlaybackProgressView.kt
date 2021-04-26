package io.github.lazyengineer.castaway.androidApp.view

import androidx.annotation.FloatRange
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap.Round
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun PlaybackProgressView(modifier: Modifier, @FloatRange(from = 0.0, to = 1.0) progress: Float) {
  Box(modifier = modifier.height(48.dp)) {
	val center = Modifier.align(Alignment.CenterStart)
	PlaybackTrack(modifier = center.fillMaxWidth(), playbackPosition = progress)
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
