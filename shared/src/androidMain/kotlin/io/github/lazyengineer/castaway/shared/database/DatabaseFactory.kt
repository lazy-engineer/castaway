package io.github.lazyengineer.castaway.shared.database

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import io.github.lazyengineer.castaway.db.CastawayDatabase
import io.github.lazyengineer.castaway.shared.entity.PlaybackPosition.Companion.playbackPositionAdapter
import iogithublazyengineercastawaydb.Episode

lateinit var appContext: Context

actual fun createDb(): CastawayDatabase {
    val driver = AndroidSqliteDriver(CastawayDatabase.Schema, appContext, "castaway.db")
    return CastawayDatabase(
        driver, episodeAdapter = Episode.Adapter(
            playbackPositionAdapter = playbackPositionAdapter
        )
    )
}