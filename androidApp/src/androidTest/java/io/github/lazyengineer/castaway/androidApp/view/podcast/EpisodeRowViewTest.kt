package io.github.lazyengineer.castaway.androidApp.view.podcast

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import org.junit.*

class EpisodeRowViewTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun episodeRowTextView_shouldDisplayEpisodeRowStateTitle() {
	composeTestRule.setContent {
	  EpisodeRowView(
		modifier = Modifier,
		state = SIMPLE_EPISODE_ROW_VIEW_STATE,
		onPlayPause = { }
	  )
	}

	composeTestRule.onNodeWithText(EPISODE_ROW_STATE_TITLE)
	  .assertIsDisplayed()
  }

  @Test
  fun episodeRowPlayIconView_shouldHasClickAction() {
	composeTestRule.setContent {
	  EpisodeRowView(
		modifier = Modifier,
		state = SIMPLE_EPISODE_ROW_VIEW_STATE,
		onPlayPause = { }
	  )
	}

	composeTestRule.onNodeWithContentDescription("play/pause")
	  .assertHasClickAction()
  }

  companion object {
	private const val EPISODE_ROW_STATE_TITLE = "Test Episode"
    private val SIMPLE_EPISODE_ROW_VIEW_STATE = EpisodeRowState(
	  playing = false,
	  title = EPISODE_ROW_STATE_TITLE,
	  progress = .3f,
	  buffering = false,
	  downloading = false,
	  played = false,
	)
  }
}
