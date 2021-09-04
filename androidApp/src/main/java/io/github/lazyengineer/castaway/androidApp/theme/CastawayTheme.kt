package io.github.lazyengineer.castaway.androidApp.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import io.github.lazyengineer.castaway.shared.resource.CastawayColorPalette
import io.github.lazyengineer.castaway.shared.resource.ThemeType
import io.github.lazyengineer.castaway.shared.resource.ThemeType.MATERIAL
import io.github.lazyengineer.castaway.shared.resource.ThemeType.NEUMORPHISM

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

  val colors: CastawayColorPalette
  	@Composable
  	get() = LocalCastawayColors.current
}

private val LocalCastawayColors = staticCompositionLocalOf<CastawayColorPalette> {
  error("No CastawayColorPalette provided")
}
