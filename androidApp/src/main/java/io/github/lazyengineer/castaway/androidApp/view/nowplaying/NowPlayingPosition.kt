package io.github.lazyengineer.castaway.androidApp.view.nowplaying

import io.github.lazyengineer.castaway.domain.entity.PlaybackPosition

data class NowPlayingPosition(
  val position: Long = 0,
  val duration: Long = 0,
) {

  val safePosition: Long
    get() = position.coerceIn(0, duration)

  companion object {

    fun PlaybackPosition.toNowPlayingPosition() = NowPlayingPosition(
      position = position,
      duration = duration,
    )

    fun NowPlayingPosition.toPlaybackPosition() = PlaybackPosition(
      position = position,
      duration = duration,
    )
  }
}
