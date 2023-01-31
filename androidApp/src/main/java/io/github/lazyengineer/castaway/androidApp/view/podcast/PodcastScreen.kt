package io.github.lazyengineer.castaway.androidApp.view.podcast

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.lazyengineer.castaway.androidApp.theme.CastawayTheme
import io.github.lazyengineer.castaway.androidApp.view.podcast.PodcastViewModel.Companion.TEST_URL
import io.github.lazyengineer.castaway.androidApp.view.podcast.PodcastViewState.Companion
import io.github.lazyengineer.castaway.domain.resource.ThemeType.MATERIAL
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.koinViewModel

@Composable
fun PodcastScreen(
  modifier: Modifier = Modifier,
  viewModel: PodcastViewModel = koinViewModel(),
  episodeSelected: (episode: PodcastEpisode) -> Unit,
) {
  val podcastState by viewModel.podcastState.collectAsStateWithLifecycle(PodcastViewState.Initial)

  PodcastScreen(
	modifier = modifier,
	state = { podcastState },
	episodeSelected = episodeSelected,
	event = remember {
	  { viewModel.podcastState.handleEvent(it) }
	},
  )
}

@Composable
internal fun PodcastScreen(
  state: () -> PodcastViewState,
  event: (PodcastEvent) -> Unit,
  modifier: Modifier = Modifier,
  episodeSelected: (episode: PodcastEpisode) -> Unit,
) {
  Surface(modifier = modifier.fillMaxSize()) {

	LaunchedEffect(Unit) {
	  event(PodcastEvent.FeedEvent.Load(TEST_URL))
	}

	state().showDetails?.let { details ->
	  LaunchedEffect(details) {
		episodeSelected(details)
		event(PodcastEvent.FeedEvent.DetailsShowed)
	  }
	}

	when (state().loading) {
	  true -> PodcastLoadingScreen()
	  false -> PodcastScreen(
		feedTitle = { state().title },
		feedImageUrl = { state().imageUrl },
		episodes = { state().episodes },
		event = event
	  )
	}
  }
}

@Composable
internal fun PodcastLoadingScreen(
  modifier: Modifier = Modifier,
) {
  Column(
	modifier = modifier.background(CastawayTheme.colors.background),
	verticalArrangement = Arrangement.Center,
	horizontalAlignment = Alignment.CenterHorizontally
  ) {
	Text(
	  text = "Loading...",
	  color = CastawayTheme.colors.onBackground,
	  fontSize = 34.sp,
	  fontWeight = FontWeight.Bold
	)
  }
}

@Composable
internal fun PodcastScreen(
  feedTitle: () -> String,
  feedImageUrl: () -> String,
  episodes: () -> EpisodesList,
  modifier: Modifier = Modifier,
  event: (PodcastEvent) -> Unit,
) {
  Surface(modifier = modifier.fillMaxSize()) {
	LazyColumn(
	  modifier = Modifier.background(CastawayTheme.colors.background),
	  state = rememberLazyListState(),
	  contentPadding = PaddingValues(0.dp),
	  horizontalAlignment = Alignment.Start
	) {
	  item {
		PodcastHeaderView(
		  modifier = Modifier.fillMaxSize(),
		  title = feedTitle(),
		  imageUrl = feedImageUrl(),
		)
	  }

	  items(episodes().items, key = { it.id }) { item ->
		EpisodeRowView(
		  state = EpisodeRowState(
			playing = item.playing,
			title = item.title,
			progress = item.playbackProgress,
		  ),
		  onPlayPause = remember {
			{ event(PodcastEvent.EpisodeRowEvent.PlayPause(itemId = item.id)) }
		  },
		  onClick = remember {
			{ event(PodcastEvent.EpisodeRowEvent.ShowDetails(item)) }
		  }
		)
	  }
	}
  }
}

@Preview
@Composable
fun PodcastScreenPreview() {
  CastawayTheme(MATERIAL, true) {
	PodcastScreen(
	  state = {
		PodcastViewState(
		  loading = false,
		  title = "Awesome Podcast",
		  imageUrl = "",
		  episodes = EpisodesList(
			listOf(
			  PodcastEpisode(
				id = "uu1d",
				title = "Awesome Episode 1",
				subTitle = "How to be just awesome!",
				audioUrl = "episode.url",
				imageUrl = "image.url",
				author = "Awesom-O",
				description = null,
				episode = 0,
				podcastUrl = "podcast.url",
				playbackPosition = 1800000L,
				playbackDuration = 2160000L,
				playbackSpeed = 1.5f,
				playing = true,
			  ),
			  PodcastEpisode(
				id = "uu2d",
				title = "Awesome Episode 2",
				subTitle = "How to be just awesome!",
				audioUrl = "episode.url",
				imageUrl = "image.url",
				author = "Awesom-O",
				description = null,
				episode = 0,
				podcastUrl = "podcast.url",
				playbackPosition = 1000000L,
				playbackDuration = 2160000L,
				playbackSpeed = 1.5f,
				playing = false,
			  )
			),
		  )
		)
	  },
	  event = {}
	) {}
  }
}

@Preview
@Composable
fun PodcastScreenLoadingPreview() {
  CastawayTheme(MATERIAL, true) {
	PodcastScreen(
	  state = {
		PodcastViewState(
		  loading = true,
		  title = "Awesome Podcast",
		  imageUrl = "podcast.url",
		  episodes = EpisodesList(emptyList()),
		)
	  },
	  event = {}
	) {}
  }
}
