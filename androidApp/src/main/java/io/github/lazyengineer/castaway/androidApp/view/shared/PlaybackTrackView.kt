@file:JvmName("PlaybackTrackViewKt")

package io.github.lazyengineer.castaway.androidApp.view.shared

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
fun PlaybackTrackView(
  @FloatRange(from = 0.0, to = 1.0) playbackPosition: Float,
  modifier: Modifier = Modifier,
  trackColor: Color = CastawayTheme.colors.primary.copy(.25f),
  progressColor: Color = CastawayTheme.colors.primary,
  thumbSize: Dp = 0.dp,
  trackStrokeWidth: Dp = 4.dp,
) {
  Canvas(modifier) {
    val thumbPx = thumbSize.toPx()
    val start = Offset(x = thumbPx, y = center.y)
    val end = Offset(x = size.width - thumbPx, y = center.y)

    drawLine(
      color = trackColor,
      start = start,
      end = end,
      strokeWidth = trackStrokeWidth.toPx(),
      cap = StrokeCap.Round
    )

    if (playbackPosition > 0) {
      val progressStart = Offset(
        x = start.x + (end.x - start.x) * 0f,
        y = center.y
      )

      val progressOffset = Offset(
        x = start.x + (end.x - start.x) * playbackPosition,
        y = center.y
      )

      drawLine(
        color = progressColor,
        start = progressStart,
        end = progressOffset,
        strokeWidth = trackStrokeWidth.toPx(),
        cap = StrokeCap.Round
      )
    }
  }
}
