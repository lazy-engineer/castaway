package io.github.lazyengineer.castaway.androidApp.view.onboarding

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import org.junit.Rule
import org.junit.Test

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

  @Test
  fun onBoardingScreen_shouldDisplayChooseThemeDescription() {
	composeTestRule.setContent {
	  OnBoardingScreen(
		modifier = Modifier,
		switchTheme = {},
		finished = {}
	  )
	}

	composeTestRule.onNodeWithContentDescription("Choose theme description")
	  .assertIsDisplayed()
  }

  @Test
  fun onBoardingScreen_shouldEqualTextIfDarkMode() {
	composeTestRule.setContent {
	  OnBoardingScreen(
		modifier = Modifier,
		darkTheme = true,
		switchTheme = {},
		finished = {}
	  )
	}

	composeTestRule.onNodeWithContentDescription("Choose theme description")
	  .assertIsDisplayed()
	  .assertTextEquals("The night is darkest just before the dawn. And I promise you, the dawn is coming.\n - Harvey Dent")
  }

  @Test
  fun onBoardingScreen_shouldEqualTextIfLightMode() {
	composeTestRule.setContent {
	  OnBoardingScreen(
		modifier = Modifier,
		darkTheme = false,
		switchTheme = {},
		finished = {}
	  )
	}

	composeTestRule.onNodeWithContentDescription("Choose theme description")
	  .assertTextEquals(
		"- Hamid: What's that? \n" +
				" - Rambo: It's blue light. \n" +
				" - Hamid: What does it do? \n" +
				" - Rambo: It turns blue."
	  )
  }
}
