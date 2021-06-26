package io.github.lazyengineer.castaway.androidApp.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import io.github.lazyengineer.castaway.androidApp.view.screen.StartScreen
import io.github.lazyengineer.castaway.androidApp.view.style.CastawayTheme
import io.github.lazyengineer.castaway.androidApp.view.style.ThemeType.NEUMORPHISM
import io.github.lazyengineer.castaway.androidApp.viewmodel.CastawayViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

  private val viewModel: CastawayViewModel by viewModel()

  override fun onCreate(savedInstanceState: Bundle?) {
	super.onCreate(savedInstanceState)
	setContent {
	  val darkMode = rememberSaveable { mutableStateOf(true) }
	  val theme = rememberSaveable { mutableStateOf(NEUMORPHISM) }

	  CastawayTheme(theme.value, darkMode.value) {
		StartScreen(viewModel) { isDarkMode ->
		  darkMode.value = isDarkMode
		}
	  }
	}
  }
}
