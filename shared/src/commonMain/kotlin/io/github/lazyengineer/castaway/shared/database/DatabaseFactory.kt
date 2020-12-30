package io.github.lazyengineer.castaway.shared.database

import com.squareup.sqldelight.db.SqlDriver

expect class DatabaseFactory {
    fun createDb(): SqlDriver
}