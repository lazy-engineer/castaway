package io.github.lazyengineer.castaway.shared.database

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import io.github.lazyengineer.castaway.db.CastawayDatabase

actual class DatabaseFactory(private val context: Context) {
    actual fun createDb(): SqlDriver {
        return AndroidSqliteDriver(CastawayDatabase.Schema, context, "castaway.db")
    }
}