package io.github.lazyengineer.castaway.androidApp.view.style

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.shadow(
  shadowColor: Int,
  paintColor: Int,
  borderRadius: Dp = 0.dp,
  shadowRadius: Dp = 20.dp,
  offsetY: Dp = 0.dp,
  offsetX: Dp = 0.dp
) = this.drawBehind {
  this.drawIntoCanvas {
	val paint = Paint()
	val frameworkPaint = paint.asFrameworkPaint()
	frameworkPaint.color = paintColor
	frameworkPaint.setShadowLayer(
	  shadowRadius.toPx(),
	  offsetX.toPx(),
	  offsetY.toPx(),
	  shadowColor
	)
	it.drawRoundRect(
	  0f,
	  0f,
	  this.size.width,
	  this.size.height,
	  borderRadius.toPx(),
	  borderRadius.toPx(),
	  paint
	)
  }
}
