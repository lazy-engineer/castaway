package io.github.lazyengineer.castaway.androidApp.view.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import io.github.lazyengineer.castaway.androidApp.theme.CastawayTheme
import io.github.lazyengineer.castaway.androidApp.view.util.screen.StartScreen
import io.github.lazyengineer.castaway.androidApp.viewmodel.CastawayViewModel
import io.github.lazyengineer.castaway.domain.resource.ThemeType.MATERIAL
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

  private val viewModel: CastawayViewModel by viewModel()

  override fun onCreate(savedInstanceState: Bundle?) {
	super.onCreate(savedInstanceState)
	setContent {
	  val systemThemeMode = isSystemInDarkTheme()
	  val darkMode = rememberSaveable { mutableStateOf(systemThemeMode) }
	  val theme = rememberSaveable { mutableStateOf(MATERIAL) }

	  CastawayTheme(theme.value, darkMode.value) {
		StartScreen(viewModel) { isDarkMode ->
		  darkMode.value = isDarkMode
		}
	  }
	}
  }
}
