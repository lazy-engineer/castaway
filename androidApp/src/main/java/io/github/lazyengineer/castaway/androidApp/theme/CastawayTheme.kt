package io.github.lazyengineer.castaway.androidApp.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import io.github.lazyengineer.castaway.androidApp.theme.ThemeType.MATERIAL
import io.github.lazyengineer.castaway.androidApp.theme.ThemeType.NEUMORPHISM
import io.github.lazyengineer.castaway.shared.resource.Colors

@Composable
fun CastawayTheme(
  themeType: ThemeType,
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable() () -> Unit) =

  when (themeType) {
	MATERIAL -> ThemeMaterial(darkTheme, content)
	NEUMORPHISM -> ThemeNeumorphism(darkTheme, content)
  }

object CastawayTheme {

  val colors: Colors
  	@Composable
  	get() = LocalCastawayColors.current
}

private val LocalCastawayColors = staticCompositionLocalOf<Colors> {
  error("No JetsnackColorPalette provided")
}