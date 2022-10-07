package io.github.lazyengineer.castaway.androidApp.view.util.screen


sealed class Screen(val route: String) {
  object Podcast : Screen("podcast")
  object NowPlaying : Screen("now_playing")
  object OnBoarding : Screen("onboarding")
}
