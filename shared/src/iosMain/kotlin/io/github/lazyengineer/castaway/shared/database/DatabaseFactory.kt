package io.github.lazyengineer.castaway.shared.database

import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import io.github.lazyengineer.castaway.db.CastawayDatabase
import io.github.lazyengineer.castaway.shared.entity.PlaybackPosition.Companion.playbackPositionAdapter
import iogithublazyengineercastawaydb.Episode

actual fun createDb(): CastawayDatabase {
    val driver = NativeSqliteDriver(CastawayDatabase.Schema, "castaway.db")
    return CastawayDatabase(
        driver, EpisodeAdapter = Episode.Adapter(
            playbackPositionAdapter = playbackPositionAdapter
        )
    )
}