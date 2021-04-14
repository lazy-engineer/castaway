package io.github.lazyengineer.castaway.androidApp.view.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import io.github.lazyengineer.castaway.androidApp.viewmodel.CastawayViewModel

@Composable
fun NowPlayingScreen(modifier: Modifier = Modifier, viewModel: CastawayViewModel, episodeId: String) {

  val feed = viewModel.feed.collectAsState()

  Surface(modifier = modifier.fillMaxSize()) {
	Text(episodeId)
  }
}