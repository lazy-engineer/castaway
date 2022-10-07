package io.github.lazyengineer.castaway.androidApp.view.util.screen

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingScreen
import io.github.lazyengineer.castaway.androidApp.view.onboarding.OnBoardingScreen
import io.github.lazyengineer.castaway.androidApp.view.podcast.PodcastScreen
import io.github.lazyengineer.castaway.androidApp.view.util.screen.Screen.NowPlaying
import io.github.lazyengineer.castaway.androidApp.view.util.screen.Screen.OnBoarding
import io.github.lazyengineer.castaway.androidApp.view.util.screen.Screen.Podcast
import io.github.lazyengineer.castaway.androidApp.viewmodel.CastawayViewModel

@Composable
fun StartScreen(viewModel: CastawayViewModel, switchTheme: (Boolean) -> Unit) {

  val navController = rememberNavController()

  NavHost(navController, startDestination = OnBoarding.route) {
	composable(OnBoarding.route) {
	  OnBoardingScreen(switchTheme = switchTheme) {
		navController.navigate(Podcast.route) {
		  popUpTo(navController.graph.startDestinationId) { inclusive = true }
		}
	  }
	}
	composable(Podcast.route) {

	  PodcastScreen(viewModel = viewModel) {
		navController.navigate(NowPlaying.route + "/${it.id}")
	  }
	}
	composable(NowPlaying.route + "/{episodeId}") { backStackEntry ->
	  NowPlayingScreen(
		viewModel = viewModel,
	  )
	}
  }
}
