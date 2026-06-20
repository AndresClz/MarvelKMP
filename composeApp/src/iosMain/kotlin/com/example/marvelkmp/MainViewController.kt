package com.example.marvelkmp

import androidx.compose.ui.window.ComposeUIViewController
import com.example.marvelkmp.data.local.DatabaseDriverFactory
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

fun MainViewController() = ComposeUIViewController {
    Napier.base(DebugAntilog())
    App(DatabaseDriverFactory())
}