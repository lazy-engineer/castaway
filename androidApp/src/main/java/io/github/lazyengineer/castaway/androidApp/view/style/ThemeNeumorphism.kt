package io.github.lazyengineer.castaway.androidApp.view.style

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import io.github.lazyengineer.castaway.shared.MR.colors

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

private fun lightColorPlatte() = Colors(
  primary = colors.colorPrimary.light.toColor(),
  primaryVariant = colors.colorAccent.light.toColor(),
  secondary = colors.colorPrimaryDark.light.toColor(),
  secondaryVariant = colors.colorPrimaryDark.light.toColor(),
  background = colors.background.light.toColor(),
  surface = colors.background.light.toColor(),
  error = Color.Red,
  onPrimary = colors.textColor.light.toColor(),
  onSecondary = colors.textColor.light.toColor(),
  onBackground = colors.textColor.light.toColor(),
  onSurface = colors.textColor.light.toColor(),
  onError = Color.White,
  isLight = true,
)

private fun darkColorPlatte() = Colors(
  primary = colors.colorPrimary.dark.toColor(),
  primaryVariant = colors.colorAccent.dark.toColor(),
  secondary = colors.colorPrimaryDark.dark.toColor(),
  secondaryVariant = colors.colorPrimaryDark.dark.toColor(),
  background = colors.background.dark.toColor(),
  surface = colors.background.dark.toColor(),
  error = Color.Red,
  onPrimary = colors.textColor.dark.toColor(),
  onSecondary = colors.textColor.dark.toColor(),
  onBackground = colors.textColor.dark.toColor(),
  onSurface = colors.textColor.dark.toColor(),
  onError = Color.White,
  isLight = false,
)

fun dev.icerock.moko.graphics.Color.toColor() = Color(rgba)
