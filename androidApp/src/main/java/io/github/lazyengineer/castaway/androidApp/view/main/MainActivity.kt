package io.github.lazyengineer.castaway.androidApp.view.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.toArgb
import io.github.lazyengineer.castaway.androidApp.theme.CastawayTheme
import io.github.lazyengineer.castaway.domain.resource.ThemeType.MATERIAL

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      val systemThemeModeDark = isSystemInDarkTheme()
      val darkMode = rememberSaveable { mutableStateOf(systemThemeModeDark) }
      val theme = rememberSaveable { mutableStateOf(MATERIAL) }

      CastawayTheme(theme.value, darkMode.value) {
        enableEdgeToEdgeBackground(
          darkMode = darkMode.value,
          backgroundColor = CastawayTheme.colors.background.toArgb()
        )

        StartScreen { isDarkMode ->
          darkMode.value = isDarkMode
        }
      }
    }
  }

  private fun enableEdgeToEdgeBackground(darkMode: Boolean, backgroundColor: Int) {
    val systemBarStyle = if (darkMode) {
      SystemBarStyle.dark(backgroundColor)
    } else {
      SystemBarStyle.light(backgroundColor, backgroundColor)
    }
    enableEdgeToEdge(statusBarStyle = systemBarStyle)
  }
}
