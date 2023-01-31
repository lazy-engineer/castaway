package io.github.lazyengineer.castaway.androidApp.view.podcast

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class PodcastHeaderViewTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun podcastHeaderView_shouldDisplayPodcastTitle() {
	val podcastTitle = "Header Test"

	composeTestRule.setContent {
	  PodcastHeaderView(
		modifier = Modifier,
		title = podcastTitle,
		imageUrl= "test-image.url"
	  )
	}

	composeTestRule.onNodeWithText(podcastTitle)
	  .assertIsDisplayed()
  }

  @Test
  fun podcastHeaderView_shouldDisplayPodcastImage() {
	composeTestRule.setContent {
	  PodcastHeaderView(
		modifier = Modifier,
		title = "Header Test",
		imageUrl= "test-image.url"
	  )
	}

	composeTestRule.onNodeWithContentDescription("Podcast header image")
	  .assertIsDisplayed()
  }
}
