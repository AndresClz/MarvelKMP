package com.example.marvelkmp.data.local

import app.cash.sqldelight.db.SqlDriver
import com.example.marvelkmp.cache.MarvelDatabase

expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}

fun createDatabase(factory: DatabaseDriverFactory): MarvelDatabase {
    val driver = factory.createDriver()
    return MarvelDatabase(driver)
}