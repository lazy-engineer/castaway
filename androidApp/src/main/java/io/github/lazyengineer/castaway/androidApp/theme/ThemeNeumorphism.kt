package io.github.lazyengineer.castaway.androidApp.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import io.github.lazyengineer.castaway.androidApp.ext.toColor
import io.github.lazyengineer.castaway.shared.resource.CastawayColorPalette
import io.github.lazyengineer.castaway.shared.resource.MaterialColorPalette

@Composable
fun ThemeNeumorphism(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {

  val sharedColorPalette = MaterialColorPalette(darkTheme)

  MaterialTheme(
	content = content,
	colors = colorPlatte(sharedColorPalette)
  )
}

@Composable
private fun colorPlatte(colors: CastawayColorPalette) = Colors(
  primary = colors.primary.color.toColor(),
  primaryVariant = colors.primaryVariant.color.toColor(),
  secondary = colors.secondary.color.toColor(),
  secondaryVariant = colors.secondaryVariant.color.toColor(),
  background = colors.background.color.toColor(),
  surface = colors.surface.color.toColor(),
  error = colors.error.color.toColor(),
  onPrimary = colors.onPrimary.color.toColor(),
  onSecondary = colors.onSecondary.color.toColor(),
  onBackground = colors.onBackground.color.toColor(),
  onSurface = colors.onSurface.color.toColor(),
  onError = colors.onError.color.toColor(),
  isLight = colors.isDark.not(),
)
