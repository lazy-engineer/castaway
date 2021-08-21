package io.github.lazyengineer.castaway.androidApp.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import io.github.lazyengineer.castaway.androidApp.theme.ThemeType.MATERIAL
import io.github.lazyengineer.castaway.androidApp.theme.ThemeType.NEUMORPHISM

@Composable
fun CastawayTheme(themeType: ThemeType, darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) =
  when (themeType) {
	MATERIAL -> ThemeMaterial(darkTheme, content)
	NEUMORPHISM -> ThemeNeumorphism(darkTheme, content)
  }
