package io.github.lazyengineer.castaway.androidApp.view

import androidx.annotation.FloatRange
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.lazyengineer.castaway.androidApp.theme.CastawayTheme

@Composable
fun PlaybackProgressView(
  modifier: Modifier = Modifier,
  @FloatRange(from = 0.0, to = 1.0) playbackPosition: Float,
  trackColor: Color = CastawayTheme.colors.primary.copy(.25f),
  progressColor: Color = CastawayTheme.colors.primary,
  padding: Dp = 16.dp,
  trackStrokeWidth: Dp = 4.dp,
) {
  Canvas(modifier) {
	val start = Offset(padding.toPx(), center.y)
	val end = Offset(size.width - padding.toPx(), center.y)

	drawLine(
	  trackColor,
	  start,
	  end,
	  trackStrokeWidth.toPx(),
	  StrokeCap.Round
	)

	if (playbackPosition > 0) {

	  val progressOffset = Offset(start.x + (end.x - start.x) * playbackPosition, center.y)
	  drawLine(
		progressColor,
		start,
		progressOffset,
		trackStrokeWidth.toPx(),
		StrokeCap.Round
	  )
	}
  }
}
