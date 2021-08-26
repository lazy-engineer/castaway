package io.github.lazyengineer.castaway.androidApp.view.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
fun TextButton(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {
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
		.background(
		  Brush.linearGradient(
			listOf(
			  Colors.azurGradientStart.toColor(),
			  Colors.azurGradientMiddle.toColor(),
			  Colors.azurGradientEnd.toColor(),
			)
		  )
		)
		.padding(horizontal = 16.dp, vertical = 8.dp),
	  contentAlignment = Alignment.Center
	) {
	  Text(text = text, color = Color.White)
	}
  }
}

@Preview
@Composable
fun TextButton_Preview() {
  TextButton(
	modifier = Modifier,
	text = "Some Button Text",
  ) {}
}
