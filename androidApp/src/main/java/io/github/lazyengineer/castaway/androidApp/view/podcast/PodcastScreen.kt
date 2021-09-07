package io.github.lazyengineer.castaway.androidApp.view.podcast

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.lazyengineer.castaway.androidApp.theme.CastawayTheme
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEpisode
import io.github.lazyengineer.castaway.androidApp.view.util.rememberFlowWithLifecycle
import io.github.lazyengineer.castaway.androidApp.viewmodel.CastawayViewModel
import io.github.lazyengineer.castaway.androidApp.viewmodel.UiEvent.EpisodeRowEvent
import io.github.lazyengineer.castaway.shared.resource.ThemeType.MATERIAL

@Composable
fun PodcastScreen(
  modifier: Modifier = Modifier,
  viewModel: CastawayViewModel,
  episodeSelected: (episode: NowPlayingEpisode) -> Unit,
) {
  val podcastState by rememberFlowWithLifecycle(viewModel.podcastState).collectAsState(PodcastViewState.Empty)

  PodcastScreen(
	modifier,
	podcastState,
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
  state: PodcastViewState,
  event: (EpisodeRowEvent) -> Unit,
  episodeSelected: (episode: NowPlayingEpisode) -> Unit,
) {
  Surface(modifier = modifier.fillMaxSize()) {

	when (state.loading) {
	  true -> PodcastLoadingScreen(modifier)
	  false -> PodcastScreen(
		modifier,
		state.title,
		state.imageUrl,
		state.episodes,
		event,
		episodeSelected
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
  modifier: Modifier = Modifier,
  feedTitle: String,
  feedImageUrl: String,
  episodes: List<NowPlayingEpisode>,
  event: (EpisodeRowEvent) -> Unit,
  episodeSelected: (episode: NowPlayingEpisode) -> Unit,
) {
  Surface(modifier = modifier.fillMaxSize()) {
	LazyColumn(
	  modifier = modifier.background(CastawayTheme.colors.background),
	  contentPadding = PaddingValues(0.dp),
	  horizontalAlignment = Alignment.Start
	) {
	  item {
		PodcastHeaderView(
		  modifier = Modifier.fillMaxSize(),
		  title = feedTitle,
		  imageUrl = feedImageUrl,
		)
	  }

	  items(episodes, key = { it.id }) { item ->
		EpisodeRowView(
		  modifier = modifier.clickable {
			event(EpisodeRowEvent.Click(item))
			episodeSelected(item)
		  },
		  state = EpisodeRowState(playing = item.playing, title = item.title, progress = item.playbackPosition.toFloat() / item.playbackDuration),
		) {
		  event(EpisodeRowEvent.PlayPause(item.id))
		}
	  }
	}
  }
}

@Preview
@Composable
fun PodcastScreen_Preview() {
  CastawayTheme(MATERIAL, true) {
	PodcastScreen(
	  state = PodcastViewState(
		loading = false,
		title = "Awesome Podcast",
		imageUrl = "",
		episodes = listOf(
		  NowPlayingEpisode(
			id = "uu1d",
			title = "Awesome Episode 1",
			subTitle = "How to be just awesome!",
			audioUrl = "episode.url",
			imageUrl = "image.url",
			author = "Awesom-O",
			playbackPosition = 1800000L,
			playbackDuration = 2160000L,
			playbackSpeed = 1.5f,
			playing = true,
		  ),
		  NowPlayingEpisode(
			id = "uu2d",
			title = "Awesome Episode 2",
			subTitle = "How to be just awesome!",
			audioUrl = "episode.url",
			imageUrl = "image.url",
			author = "Awesom-O",
			playbackPosition = 1000000L,
			playbackDuration = 2160000L,
			playbackSpeed = 1.5f,
			playing = false,
		  )
		),
	  ),
	  event = {}
	) {}
  }
}

@Preview
@Composable
fun PodcastScreen_Loading_Preview() {
  CastawayTheme(MATERIAL, true) {
	PodcastScreen(
	  state = PodcastViewState(
		loading = true,
		title = "Awesome Podcast",
		imageUrl = "podcast.url",
		episodes = listOf(),
	  ),
	  event = {}
	) {}
  }
}
