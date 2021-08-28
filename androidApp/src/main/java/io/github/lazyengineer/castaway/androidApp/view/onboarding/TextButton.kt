package io.github.lazyengineer.castaway.androidApp.view.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
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
import io.github.lazyengineer.castaway.shared.resource.Colors

@Composable
fun TextButton(
  modifier: Modifier = Modifier,
  text: String,
  textColor: Color = MaterialTheme.colors.onSurface,
  color: Color,
  onClick: () -> Unit
) {
  TextButton(
	modifier = modifier,
	text = text,
	textColor = textColor,
	color = color,
	shape = CircleShape,
	onClick = onClick
  )
}

@Composable
fun GradientTextButton(
  modifier: Modifier = Modifier,
  text: String,
  textColor: Color = MaterialTheme.colors.onSurface,
  gradient: Brush,
  onClick: () -> Unit
) {
  TextButton(
	modifier = modifier,
	text = text,
	textColor = textColor,
	gradient = gradient,
	shape = CircleShape,
	onClick = onClick
  )
}

@Composable
internal fun TextButton(
  modifier: Modifier = Modifier,
  text: String,
  textColor: Color = MaterialTheme.colors.onSurface,
  color: Color? = null,
  gradient: Brush? = null,
  shape: Shape,
  onClick: () -> Unit,
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
	  Text(text = text, color = textColor)
	}
  }
}

@Preview
@Composable
fun TextButton_Preview() {
  TextButton(
	modifier = Modifier,
	text = "Some Button Text",
	color = MaterialTheme.colors.surface,
  ) {}
}

@Preview
@Composable
fun GradientTextButton_Preview() {
  GradientTextButton(
	modifier = Modifier,
	text = "Some Button Text",
	textColor = Color.White,
	gradient = Brush.linearGradient(
	  listOf(
		Colors.azurGradientStart.toColor(),
		Colors.azurGradientMiddle.toColor(),
		Colors.azurGradientEnd.toColor(),
	  )
	),
  ) {}
}
