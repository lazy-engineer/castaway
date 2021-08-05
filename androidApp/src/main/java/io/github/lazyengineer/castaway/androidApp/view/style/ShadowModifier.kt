package io.github.lazyengineer.castaway.androidApp.view.style

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Credits: https://gist.github.com/arthurgonzaga/598267f570e38425fc52f97b30e0619d
 */
fun Modifier.shadow(
  color: Color,
  borderRadius: Dp = 12.dp,
  shadowRadius: Dp = 20.dp,
  offsetY: Dp = 0.dp,
  offsetX: Dp = 0.dp
) = composed {

  val shadowColor = color.toArgb()
  val transparent = color.copy(alpha = .2f).toArgb()

  this.drawBehind {

	this.drawIntoCanvas {
	  val paint = Paint()
	  val frameworkPaint = paint.asFrameworkPaint()
	  frameworkPaint.color = transparent

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
}
