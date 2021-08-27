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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.lazyengineer.castaway.androidApp.ext.toColor
import io.github.lazyengineer.castaway.shared.resource.Colors

@Composable
fun GradientTextButton(
  modifier: Modifier = Modifier,
  text: String,
  textColor: Color = MaterialTheme.colors.onSurface,
  gradient: Brush,
  onClick: () -> Unit
) {
  Button(
	colors = ButtonDefaults.buttonColors(
	  backgroundColor = Color.Transparent
	),
	contentPadding = PaddingValues(),
	shape = CircleShape,
	onClick = onClick
  ) {
	Box(
	  modifier = modifier
		.background(gradient)
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
  GradientTextButton(
	modifier = Modifier,
	text = "Some Button Text",
	gradient = Brush.linearGradient(
	  listOf(
		Colors.azurGradientStart.toColor(),
		Colors.azurGradientMiddle.toColor(),
		Colors.azurGradientEnd.toColor(),
	  )
	)
  ) {}
}
