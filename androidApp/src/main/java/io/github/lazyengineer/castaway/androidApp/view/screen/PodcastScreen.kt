package io.github.lazyengineer.castaway.androidApp.view.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.lazyengineer.castaway.androidApp.view.EpisodeRowView
import io.github.lazyengineer.castaway.androidApp.view.PodcastHeaderView
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
		PodcastHeaderView(
		  modifier = Modifier.fillMaxSize(),
		  title = feed.value?.info?.title ?: "Some Awesome Podcast",
		  imageUrl = feed.value?.info?.imageUrl ?: "",
		)
	  }

	  items(feed.value?.episodes ?: emptyList()) { item ->
		EpisodeRowView(modifier = modifier.clickable {
		  viewModel.episodeClicked(item)
		}, title = item.title) {
		  viewModel.mediaItemClicked(item.id)
		}
	  }
	}
  }
}