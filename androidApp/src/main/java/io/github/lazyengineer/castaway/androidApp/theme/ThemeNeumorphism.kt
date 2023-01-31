package io.github.lazyengineer.castaway.androidApp.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import io.github.lazyengineer.castaway.domain.resource.MaterialColorPalette

@Composable
fun ThemeNeumorphism(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {

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
