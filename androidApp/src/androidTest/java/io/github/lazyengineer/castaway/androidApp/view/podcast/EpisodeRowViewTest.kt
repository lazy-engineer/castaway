package io.github.lazyengineer.castaway.androidApp.view.podcast

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.*

class EpisodeRowViewTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun episodeRowTest() {
	composeTestRule.setContent {
	  EpisodeRowView(
		modifier = Modifier,
		state = EpisodeRowState.Empty,
		onPlayPause = { }
	  )
	}
	Thread.sleep(5000)
  }
}