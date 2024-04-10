package io.github.lazyengineer.castaway.androidApp.view.main

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.lazyengineer.castaway.androidApp.view.onboarding.OnBoardingScreen
import io.github.lazyengineer.castaway.androidApp.view.util.screen.Screen.NowPlaying
import io.github.lazyengineer.castaway.androidApp.view.util.screen.Screen.OnBoarding
import io.github.lazyengineer.castaway.androidApp.view.util.screen.Screen.Podcast

@Composable
fun StartScreen(switchTheme: (Boolean) -> Unit) {

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
      Home()
    }
    composable(NowPlaying.route + "/{episodeId}") { backStackEntry ->
      backStackEntry.arguments?.getString("episodeId")?.let { episodeId ->
        Log.d("NavHost", backStackEntry.arguments.toString())
      }
    }
  }
}
