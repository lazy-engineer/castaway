package io.github.lazyengineer.castaway.androidApp.view.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.lazyengineer.castaway.androidApp.viewmodel.CastawayViewModel

@Composable
fun PodcastScreen(modifier: Modifier = Modifier, viewModel: CastawayViewModel) {

  Surface(color = Color.Red, modifier = modifier.fillMaxSize()) {
    LazyColumn(
      modifier = modifier,
      contentPadding = PaddingValues(0.dp),
      horizontalAlignment = Alignment.Start
    ) {

    }
  }
}