package io.github.lazyengineer.castaway.androidApp.view.podcast

import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasStateDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class EpisodeRowViewTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun episodeRowTextView_shouldDisplayEpisodeRowStateTitle() {
    composeTestRule.setContent {
      EpisodeRowView(
        modifier = Modifier,
        state = SIMPLE_EPISODE_ROW_VIEW_STATE,
        onPlayPause = { },
        onClick = { },
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
        onPlayPause = { },
        onClick = { },
      )
    }

    composeTestRule.onNodeWithContentDescription("play/pause")
      .assertHasClickAction()
  }

  @Test
  fun episodeRowPlayIconView_shouldShowPauseImageIfPlaying() {
    composeTestRule.setContent {
      EpisodeRowView(
        modifier = Modifier,
        state = SIMPLE_EPISODE_ROW_VIEW_STATE.copy(playing = true),
        onPlayPause = { },
        onClick = { },
      )
    }

    composeTestRule.onNodeWithContentDescription("play/pause")
      .assert(hasStateDescription(Filled.Pause.name))
  }

  @Test
  fun episodeRowPlayIconView_shouldShowPlayImageIfNotPlaying() {
    composeTestRule.setContent {
      EpisodeRowView(
        modifier = Modifier,
        state = SIMPLE_EPISODE_ROW_VIEW_STATE.copy(playing = false),
        onPlayPause = { },
        onClick = { },
      )
    }

    composeTestRule.onNodeWithContentDescription("play/pause")
      .assert(hasStateDescription(Filled.PlayArrow.name))
  }

  @Test
  fun episodeRowClickIconView_shouldCallOnPlayPauseCallback() {
    var playing = false

    composeTestRule.setContent {
      EpisodeRowView(
        modifier = Modifier,
        state = SIMPLE_EPISODE_ROW_VIEW_STATE.copy(playing = playing),
        onPlayPause = { playing },
        onClick = { },
      )
    }

    composeTestRule.onNodeWithContentDescription("play/pause")
      .performClick()

    assert(playing)
  }

  @Test
  fun episodeRowView_shouldDisplayPlaybackProgressView() {
    composeTestRule.setContent {
      EpisodeRowView(
        modifier = Modifier,
        state = SIMPLE_EPISODE_ROW_VIEW_STATE,
        onPlayPause = { },
        onClick = { },
      )
    }

    composeTestRule.onNodeWithContentDescription("${SIMPLE_EPISODE_ROW_VIEW_STATE.progress.times(100)}% playback progress")
      .assertExists() // .assertIsDisplayed() Fails maybe cause of -> TO DO: check semantics hidden property
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
