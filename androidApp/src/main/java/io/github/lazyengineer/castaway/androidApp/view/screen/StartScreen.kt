package io.github.lazyengineer.castaway.androidApp.view.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import io.github.lazyengineer.castaway.androidApp.viewmodel.CastawayViewModel

@Composable
fun StartScreen(viewModel: CastawayViewModel) {

  val onBoardingFinished = rememberSaveable { mutableStateOf(false) }

  Box(modifier = Modifier.fillMaxSize()) {
	if (onBoardingFinished.value.not()) {
	  OnBoardingScreen(Modifier.zIndex(1f)) { finished ->
		onBoardingFinished.value = finished
	  }
	}

	PodcastScreen(viewModel = viewModel)
  }
}