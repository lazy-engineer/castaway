package io.github.lazyengineer.castaway.androidApp.view.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.lazyengineer.castaway.androidApp.view.EpisodeRowView
import io.github.lazyengineer.castaway.androidApp.viewmodel.CastawayViewModel

@Composable
fun PodcastScreen(modifier: Modifier = Modifier, viewModel: CastawayViewModel) {

  val feed = viewModel.feed.collectAsState()

  Surface(modifier = modifier.fillMaxSize()) {
	LazyColumn(
	  modifier = modifier,
	  contentPadding = PaddingValues(0.dp),
	  horizontalAlignment = Alignment.Start
	) {
	  item {
		Text(feed.value?.info?.title ?: "Wow Header")
	  }

	  items(feed.value?.episodes ?: emptyList()) { item ->
		EpisodeRowView(modifier = modifier.clickable { }, title = item.title) {}
	  }
	}
  }
}