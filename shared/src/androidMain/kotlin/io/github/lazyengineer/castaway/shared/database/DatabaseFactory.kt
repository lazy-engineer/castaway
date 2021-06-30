package io.github.lazyengineer.castaway.shared.database

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import io.github.lazyengineer.castaway.db.CastawayDatabase
import io.github.lazyengineer.castaway.shared.entity.PlaybackPosition.Companion.playbackPositionAdapter
import iogithublazyengineercastawaydb.EpisodeEntity

lateinit var appContext: Context

actual fun createDb(): CastawayDatabase {
  val driver = AndroidSqliteDriver(CastawayDatabase.Schema, appContext, "castaway.db")
  return CastawayDatabase(
	driver, episodeEntityAdapter = EpisodeEntity.Adapter(
	  playbackPositionAdapter = playbackPositionAdapter
	)
  )
}
