package io.github.lazyengineer.castaway.androidApp.view.style

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import io.github.lazyengineer.castaway.androidApp.view.style.ThemeType.NEUMORPHISM

@Composable
fun CastawayTheme(themeType: ThemeType, darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) =
  when (themeType) {
	NEUMORPHISM -> ThemeNeumorphism(darkTheme, content)
  }
