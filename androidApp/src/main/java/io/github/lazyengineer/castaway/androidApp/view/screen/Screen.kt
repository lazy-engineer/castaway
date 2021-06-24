package io.github.lazyengineer.castaway.androidApp.view.screen


sealed class Screen(val route: String) {
  object Podcast : Screen("podcast")
  object NowPlaying : Screen("now_playing")
  object OnBoarding : Screen("onboarding")
}
