package io.github.lazyengineer.castawayplayer

data class PlaybackState(
  val isPrepared: Boolean,
  val isPlayEnabled: Boolean,
  val isPlaying: Boolean,
  val currentPlaybackPosition: Long,
)
