package io.github.lazyengineer.castaway.androidApp.view.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.lazyengineer.castaway.androidApp.ext.toColor
import io.github.lazyengineer.castaway.androidApp.theme.CastawayTheme
import io.github.lazyengineer.castaway.domain.resource.Colors

@Composable
fun TextButton(
  text: String,
  color: Color,
  modifier: Modifier = Modifier,
  textColor: Color = CastawayTheme.colors.onSurface,
  shape: Shape = CircleShape,
  onClick: () -> Unit
) {
  TextButton(
	modifier = modifier,
	color = color,
	shape = shape,
	onClick = onClick
  ) {
	this.ButtonText(text = text, color = textColor)
  }
}

@Composable
fun GradientTextButton(
  text: String,
  gradient: Brush,
  modifier: Modifier = Modifier,
  textColor: Color = CastawayTheme.colors.onSurface,
  shape: Shape = CircleShape,
  onClick: () -> Unit
) {
  TextButton(
	modifier = modifier,
	gradient = gradient,
	shape = shape,
	onClick = onClick
  ) {
	this.ButtonText(text = text, color = textColor)
  }
}

@Composable
internal fun TextButton(
  shape: Shape,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  color: Color? = null,
  gradient: Brush? = null,
  text: @Composable TextButtonScope.() -> Unit
) {
  Button(
	colors = ButtonDefaults.buttonColors(
	  backgroundColor = Color.Transparent
	),
	contentPadding = PaddingValues(),
	shape = shape,
	onClick = onClick
  ) {
	val backgroundModifier = if (gradient != null) {
	  modifier.background(gradient)
	} else {
	  color?.let { modifier.background(color) } ?: modifier
	}
	Box(
	  modifier = backgroundModifier
		.padding(horizontal = 16.dp, vertical = 8.dp),
	  contentAlignment = Alignment.Center
	) {
	  TextButtonScope.text()
	}
  }
}

@LayoutScopeMarker
interface TextButtonScope {

  @Composable
  fun ButtonText(text: String, color: Color) {
	Text(text = text, color = color)
  }

  companion object : TextButtonScope
}

@Preview
@Composable
fun TextButtonPreview() {
  TextButton(
	modifier = Modifier,
	text = "Some Button Text",
	color = CastawayTheme.colors.surface,
  ) {}
}

@Preview
@Composable
fun GradientTextButtonPreview() {
  GradientTextButton(
	modifier = Modifier,
	text = "Some Button Text",
	textColor = Color.White,
	shape = RoundedCornerShape(8.dp),
	gradient = Brush.linearGradient(
	  listOf(
		Colors.azurGradientStart.toColor(),
		Colors.azurGradientMiddle.toColor(),
		Colors.azurGradientEnd.toColor(),
	  )
	),
  ) {}
}
