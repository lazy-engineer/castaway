package io.github.lazyengineer.castaway.androidApp.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import io.github.lazyengineer.castaway.androidApp.databinding.ActivityMainBinding
import io.github.lazyengineer.castaway.androidApp.view.screen.StartScreen
import io.github.lazyengineer.castaway.androidApp.view.style.ThemeNeumorphism
import io.github.lazyengineer.castaway.androidApp.viewmodel.CastawayViewModel
import io.github.lazyengineer.castaway.shared.Greeting
import org.koin.androidx.viewmodel.ext.android.viewModel

fun greet(): String {
  return Greeting().greeting()
}

class MainActivity : AppCompatActivity() {

  private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
  private val viewModel: CastawayViewModel by viewModel()

  override fun onCreate(savedInstanceState: Bundle?) {
	super.onCreate(savedInstanceState)
	setContent {
	  ThemeNeumorphism {
		StartScreen(viewModel)
	  }
	}
  }
}
