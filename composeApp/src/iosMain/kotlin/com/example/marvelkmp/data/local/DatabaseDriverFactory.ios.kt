package com.example.marvelkmp.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.example.marvelkmp.cache.MarvelDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(MarvelDatabase.Schema, "marvel.db")
    }
}