package io.github.lazyengineer.castawayplayer.extention

import android.os.SystemClock
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY
import android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY_PAUSE
import android.support.v4.media.session.PlaybackStateCompat.STATE_BUFFERING
import android.support.v4.media.session.PlaybackStateCompat.STATE_PAUSED
import android.support.v4.media.session.PlaybackStateCompat.STATE_PLAYING

inline val PlaybackStateCompat.isPrepared
  get() = state == STATE_BUFFERING ||
    state == STATE_PLAYING ||
    state == STATE_PAUSED

inline val PlaybackStateCompat.isPlaying
  get() = state == STATE_BUFFERING ||
    state == STATE_PLAYING

inline val PlaybackStateCompat.isPlayEnabled
  get() = actions and ACTION_PLAY != 0L ||
    (actions and ACTION_PLAY_PAUSE != 0L &&
      state == STATE_PAUSED)

inline val PlaybackStateCompat.currentPlaybackPosition: Long
  get() = if (state == STATE_PLAYING) {
    val timeDelta = SystemClock.elapsedRealtime() - lastPositionUpdateTime
    (position + (timeDelta * playbackSpeed)).toLong()
  } else position

val EMPTY_PLAYBACK_STATE: PlaybackStateCompat = PlaybackStateCompat.Builder()
  .setState(PlaybackStateCompat.STATE_NONE, 0, 0f)
  .build()
