package io.github.lazyengineer.castaway.shared.database

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import io.github.lazyengineer.castaway.db.CastawayDatabase

actual class DatabaseFactory {
    actual fun createDb(): SqlDriver {
        return NativeSqliteDriver(CastawayDatabase.Schema, "castaway.db")
    }
}