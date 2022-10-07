package io.github.lazyengineer.castaway.data.database.adapter

import com.squareup.sqldelight.ColumnAdapter
import io.github.lazyengineer.castaway.domain.entity.PlaybackPosition

object PlaybackPositionAdapter : ColumnAdapter<PlaybackPosition, String> {

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
