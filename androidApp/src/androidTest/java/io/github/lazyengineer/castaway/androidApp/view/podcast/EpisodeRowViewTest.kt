package io.github.lazyengineer.castaway.androidApp.view.podcast

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.*

class EpisodeRowViewTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun episodeRowTextView_shouldDisplayEpisodeRowStateTitle() {
	val episodeRowStateTitle = "Test Episode"

	composeTestRule.setContent {
	  EpisodeRowView(
		modifier = Modifier,
		state = EpisodeRowState(
		  playing = false,
		  title = episodeRowStateTitle,
		  progress = .3f,
		  buffering = false,
		  downloading = false,
		  played = false,
		),
		onPlayPause = { }
	  )
	}

	composeTestRule.onNodeWithText(episodeRowStateTitle)
	  .assertIsDisplayed()
  }
}
