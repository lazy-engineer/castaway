package io.github.lazyengineer.castaway.androidApp.view.style

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import io.github.lazyengineer.castaway.androidApp.R

@Composable
fun ThemeNeumorphism(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {

  val colorPalette = if (darkTheme) {
	darkColorPlatte()
  } else {
	lightColorPlatte()
  }

  MaterialTheme(
	content = content,
	colors = colorPalette
  )
}

@Composable
private fun lightColorPlatte() = Colors(
  primary = colorResource(R.color.blueGradientStart),
  primaryVariant = colorResource(R.color.blueGradientMiddle),
  secondary = colorResource(R.color.blueGradientEnd),
  secondaryVariant = colorResource(R.color.blueGradientEnd),
  background = colorResource(R.color.lightThemeBackground),
  surface = colorResource(R.color.lightThemeBackground),
  error = Color.Red,
  onPrimary = colorResource(R.color.lightThemeTextColor),
  onSecondary = colorResource(R.color.lightThemeTextColor),
  onBackground = colorResource(R.color.lightThemeTextColor),
  onSurface = colorResource(R.color.lightThemeTextColor),
  onError = Color.White,
  isLight = true,
)

@Composable
private fun darkColorPlatte() = Colors(
  primary = colorResource(id = R.color.orangeGradientStart),
  primaryVariant = colorResource(R.color.orangeGradientMiddle),
  secondary = colorResource(R.color.orangeGradientEnd),
  secondaryVariant = colorResource(R.color.orangeGradientEnd),
  background = colorResource(R.color.darkThemeBackground),
  surface = colorResource(R.color.darkThemeBackground),
  error = Color.Red,
  onPrimary = colorResource(R.color.darkThemeTextColor),
  onSecondary = colorResource(R.color.darkThemeTextColor),
  onBackground = colorResource(R.color.darkThemeTextColor),
  onSurface = colorResource(R.color.darkThemeTextColor),
  onError = Color.White,
  isLight = false,
)
