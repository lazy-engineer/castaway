package io.github.lazyengineer.castaway.androidApp.view.util.screen


sealed class Screen(val route: String) {
  data object Podcast : Screen("podcast")
  data object NowPlaying : Screen("now_playing")
  data object OnBoarding : Screen("onboarding")
}
