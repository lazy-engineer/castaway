package io.github.lazyengineer.castaway.androidApp.view.nowplaying

sealed class NowPlayingState {
  object Loading : NowPlayingState()
  data class Playing(val episode: NowPlayingEpisode) : NowPlayingState()
  data class Paused(val episode: NowPlayingEpisode) : NowPlayingState()
  object Buffering : NowPlayingState()
  object Played : NowPlayingState()
}