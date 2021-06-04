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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.lazyengineer.castaway.androidApp.view.EpisodeRowState
import io.github.lazyengineer.castaway.androidApp.view.EpisodeRowView
import io.github.lazyengineer.castaway.androidApp.view.PodcastHeaderView
import io.github.lazyengineer.castaway.androidApp.view.screen.PodcastState.Loaded
import io.github.lazyengineer.castaway.androidApp.view.util.rememberFlowWithLifecycle
import io.github.lazyengineer.castaway.androidApp.viewmodel.CastawayViewModel
import io.github.lazyengineer.castaway.androidApp.viewmodel.UiEvent.EpisodeRowEvent
import io.github.lazyengineer.castaway.shared.entity.Episode
import io.github.lazyengineer.castaway.shared.entity.FeedData

@Composable
fun PodcastScreen(
  modifier: Modifier = Modifier,
  viewModel: CastawayViewModel,
  episodeSelected: (episode: Episode) -> Unit,
) {
  val episodeState by rememberFlowWithLifecycle(viewModel.episodeRowState).collectAsState(EpisodeRowState.Unplayed)
  val podcastState by rememberFlowWithLifecycle(viewModel.podcastState).collectAsState(PodcastState.Loading)

  PodcastScreen(
	modifier,
	podcastState,
	episodeState,
	event = {
	  viewModel.submitEvent(it)
	},
	episodeSelected = { episode ->
	  episodeSelected(episode)
	}
  )
}

@Composable
internal fun PodcastScreen(
  modifier: Modifier = Modifier,
  state: PodcastState,
  episodeState: EpisodeRowState,
  event: (EpisodeRowEvent) -> Unit,
  episodeSelected: (episode: Episode) -> Unit,
) {
  Surface(modifier = modifier.fillMaxSize()) {

	when (state) {
	  PodcastState.Loading -> PodcastLoadingScreen(modifier)
	  is Loaded -> PodcastScreen(modifier, state.loadedFeed, episodeState, event, episodeSelected)
	}
  }
}

@Composable
internal fun PodcastLoadingScreen(
  modifier: Modifier = Modifier
) {
  Surface(modifier = modifier.fillMaxSize()) {
	Text("Loading...")
  }
}

@Composable
internal fun PodcastScreen(
  modifier: Modifier = Modifier,
  feed: FeedData,
  episodeState: EpisodeRowState,
  event: (EpisodeRowEvent) -> Unit,
  episodeSelected: (episode: Episode) -> Unit,
) {
  Surface(modifier = modifier.fillMaxSize()) {
	LazyColumn(
	  modifier = modifier,
	  contentPadding = PaddingValues(0.dp),
	  horizontalAlignment = Alignment.Start
	) {
	  item {
		PodcastHeaderView(
		  modifier = Modifier.fillMaxSize(),
		  title = feed.info.title,
		  imageUrl = feed.info.imageUrl ?: "",
		)
	  }

	  items(feed.episodes, key = { it.id }) { item ->
		EpisodeRowView(
		  modifier = modifier.clickable {
			event(EpisodeRowEvent.Click(item))
			episodeSelected(item)
		  },
		  state = episodeState,
		  title = item.title,
		  progress = item.playbackPosition.position.toFloat() / item.playbackPosition.duration
		) {
		  event(EpisodeRowEvent.PlayPause(item.id))
		}
	  }
	}
  }
}

sealed class PodcastState(val feed: FeedData? = null) {
  object Loading : PodcastState()
  data class Loaded(val loadedFeed: FeedData) : PodcastState(loadedFeed)
}