package io.github.lazyengineer.castaway.shared.entity

import com.squareup.sqldelight.ColumnAdapter

data class PlaybackPosition(
  val position: Long = 0,
  val duration: Long = 1,
) {

  companion object {

	val playbackPositionAdapter = object : ColumnAdapter<PlaybackPosition, String> {
	  override fun decode(databaseValue: String): PlaybackPosition {
		val playbackPosition = databaseValue.split(",")
		val position = playbackPosition[0].toLong()
		val duration = playbackPosition[1].toLong()

		return PlaybackPosition(position, duration)
	  }

	  override fun encode(value: PlaybackPosition): String {
		return "${value.position},${value.duration}"
	  }
	}
  }
}
