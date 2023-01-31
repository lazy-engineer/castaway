package io.github.lazyengineer.castaway.androidApp.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import io.github.lazyengineer.castaway.domain.resource.MaterialColorPalette

@Composable
fun ThemeMaterial(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {

  val sharedColorPalette = MaterialColorPalette(darkTheme)

  ProvideCastawayColors(colorsPalette = sharedColorPalette) {
	MaterialTheme(
	  colors = debugColors(darkTheme)
	) {
	  CompositionLocalProvider(LocalRippleTheme provides CastawayRippleTheme) {
		content()
	  }
	}
  }
}

object CastawayRippleTheme : RippleTheme {

  // Here you should return the ripple color you want
  // and not use the defaultRippleColor extension on RippleTheme.
  // Using that will override the ripple color set in DarkMode
  // or when you set light parameter to false
  @Composable
  override fun defaultColor(): Color = CastawayTheme.colors.primary

  @Composable
  override fun rippleAlpha(): RippleAlpha = RippleTheme.defaultRippleAlpha(
	Color.Black,
	lightTheme = !isSystemInDarkTheme()
  )
}
