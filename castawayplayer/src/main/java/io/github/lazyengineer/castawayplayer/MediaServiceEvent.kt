package io.github.lazyengineer.castawayplayer

sealed class MediaServiceEvent {
  data class PlayMediaId(val mediaId: String, val pauseAllowed: Boolean = true) : MediaServiceEvent()
  data class Speed(val speed: Float) : MediaServiceEvent()
  data class Shuffle(val shuffle: Boolean) : MediaServiceEvent()
  data class RepeatMode(val repeat: Int) : MediaServiceEvent()
  data class SeekTo(val position: Long) : MediaServiceEvent()
  data object FastForward : MediaServiceEvent()
  data object Rewind : MediaServiceEvent()
  data object SkipToNext : MediaServiceEvent()
  data object SkipToPrevious : MediaServiceEvent()
}
