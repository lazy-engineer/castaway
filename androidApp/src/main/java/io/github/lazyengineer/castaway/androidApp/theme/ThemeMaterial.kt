package io.github.lazyengineer.castaway.androidApp.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import io.github.lazyengineer.castaway.domain.resource.MaterialColorPalette

@Composable
fun ThemeMaterial(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {

  val sharedColorPalette = MaterialColorPalette(darkTheme)

  ProvideCastawayColors(colorsPalette = sharedColorPalette) {
	MaterialTheme(
	  content = content,
	  colors = debugColors(darkTheme)
	)
  }
}
