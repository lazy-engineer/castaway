package io.github.lazyengineer.castaway.androidApp.view.onboarding

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import org.junit.*

class OnBoardingScreenTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun onBoardingScreen_shouldDisplayChooseThemeTitle() {
	composeTestRule.setContent {
	  OnBoardingScreen(
		modifier = Modifier,
		switchTheme = {},
		finished = {}
	  )
	}

	composeTestRule.onNodeWithContentDescription("Choose theme title")
	  .assertIsDisplayed()
  }
}