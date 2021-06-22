package io.github.lazyengineer.castaway.androidApp.view.style

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import io.github.lazyengineer.castaway.shared.MR

@Composable
fun ThemeNeumorphism(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {

  val colorPalette = if (darkTheme) {
	Colors(
	  primary = MR.colors.colorPrimary.dark.toColor(),
	  primaryVariant = MR.colors.colorAccent.dark.toColor(),
	  secondary = MR.colors.colorPrimaryDark.dark.toColor(),
	  secondaryVariant = MR.colors.colorPrimaryDark.dark.toColor(),
	  background = MR.colors.background.dark.toColor(),
	  surface = MR.colors.background.dark.toColor(),
	  error = Color.Red,
	  onPrimary = MR.colors.textColor.dark.toColor(),
	  onSecondary = MR.colors.textColor.dark.toColor(),
	  onBackground = MR.colors.textColor.dark.toColor(),
	  onSurface = MR.colors.textColor.dark.toColor(),
	  onError = Color.White,
	  isLight = false,
	)
  } else {
	Colors(
	  primary = MR.colors.colorPrimary.light.toColor(),
	  primaryVariant = MR.colors.colorAccent.light.toColor(),
	  secondary = MR.colors.colorPrimaryDark.light.toColor(),
	  secondaryVariant = MR.colors.colorPrimaryDark.light.toColor(),
	  background = MR.colors.background.light.toColor(),
	  surface = MR.colors.background.light.toColor(),
	  error = Color.Red,
	  onPrimary = MR.colors.textColor.light.toColor(),
	  onSecondary = MR.colors.textColor.light.toColor(),
	  onBackground = MR.colors.textColor.light.toColor(),
	  onSurface = MR.colors.textColor.light.toColor(),
	  onError = Color.White,
	  isLight = true,
	)
  }

  MaterialTheme(
	content = content,
	colors = colorPalette
  )
}

fun dev.icerock.moko.graphics.Color.toColor() = Color(
  red = this.red,
  blue = this.blue,
  green = this.green,
  alpha = this.alpha,
)
