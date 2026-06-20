package com.example.marvelkmp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.example.marvelkmp.data.local.DatabaseDriverFactory
import com.example.marvelkmp.ui.screens.home.HomeScreen

val LocalDatabaseDriverFactory =
    staticCompositionLocalOf<DatabaseDriverFactory> {
        error("DatabaseDriverFactory not provided")
    }

@Composable
fun App(driverFactory: DatabaseDriverFactory) {
    CompositionLocalProvider(LocalDatabaseDriverFactory provides driverFactory) {
        MaterialTheme {
            Navigator(HomeScreen()) { navigator ->
                SlideTransition(navigator)
            }
        }
    }
}
