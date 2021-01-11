package io.github.lazyengineer.castaway.shared.database

import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import io.github.lazyengineer.castaway.db.CastawayDatabase
import io.github.lazyengineer.castaway.shared.entity.PlaybackPosition.Companion.playbackPositionAdapter
import iogithublazyengineercastawaydb.EpisodeEntity

actual fun createDb(): CastawayDatabase {
    val driver = NativeSqliteDriver(CastawayDatabase.Schema, "castaway.db")
    return CastawayDatabase(
        driver, episodeEntityAdapter = EpisodeEntity.Adapter(
            playbackPositionAdapter = playbackPositionAdapter
        )
    )
}