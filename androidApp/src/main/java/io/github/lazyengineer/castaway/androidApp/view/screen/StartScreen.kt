package io.github.lazyengineer.castaway.androidApp.view.screen

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingScreen
import io.github.lazyengineer.castaway.androidApp.view.onboarding.OnBoardingScreen
import io.github.lazyengineer.castaway.androidApp.view.podcast.PodcastScreen
import io.github.lazyengineer.castaway.androidApp.viewmodel.CastawayViewModel

@Composable
fun StartScreen(viewModel: CastawayViewModel, switchTheme: (Boolean) -> Unit) {

  val navController = rememberNavController()

  NavHost(navController, startDestination = Screen.OnBoarding.route) {
	composable(Screen.OnBoarding.route) {
	  OnBoardingScreen(switchTheme = switchTheme) {
		navController.navigate(Screen.Podcast.route) {
		  popUpTo(navController.graph.startDestinationId) { inclusive = true }
		}
	  }
	}
	composable(Screen.Podcast.route) {

	  PodcastScreen(viewModel = viewModel) {
		navController.navigate(Screen.NowPlaying.route + "/${it.id}")
	  }
	}
	composable(Screen.NowPlaying.route + "/{episodeId}") { backStackEntry ->
	  NowPlayingScreen(
		viewModel = viewModel,
	  )
	}
  }
}
