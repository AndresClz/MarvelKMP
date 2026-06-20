package com.example.marvelkmp

import androidx.compose.ui.window.ComposeUIViewController
import com.example.marvelkmp.data.local.DatabaseDriverFactory
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

private val napierInit by lazy { Napier.base(DebugAntilog()) }

fun MainViewController() = ComposeUIViewController {
    napierInit
    App(DatabaseDriverFactory())
}