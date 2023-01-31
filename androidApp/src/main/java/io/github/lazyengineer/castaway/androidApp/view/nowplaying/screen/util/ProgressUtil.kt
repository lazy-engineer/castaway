package io.github.lazyengineer.castaway.androidApp.view.nowplaying.screen.util

import java.util.Locale
import java.util.concurrent.TimeUnit.HOURS
import java.util.concurrent.TimeUnit.MILLISECONDS
import java.util.concurrent.TimeUnit.MINUTES

internal fun playbackProgress(
  playbackPosition: Long,
  playbackDuration: Long
): Float {
  return when {
	playbackDuration <= 0 -> 0f
	else -> playbackPosition / playbackDuration.toFloat()
  }
}

internal fun Float.progressToPosition(playbackDuration: Long) = (this * playbackDuration).toLong()

internal fun Long.millisToTxt() = String.format(
  locale = Locale.getDefault(),
  format = "%02d:%02d:%02d",
  MILLISECONDS.toHours(this),
  MILLISECONDS.toMinutes(this) - HOURS.toMinutes(MILLISECONDS.toHours(this)),
  MILLISECONDS.toSeconds(this) - MINUTES.toSeconds(MILLISECONDS.toMinutes(this))
)
