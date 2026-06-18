package com.example.marvelkmp

import androidx.compose.ui.window.ComposeUIViewController
import com.example.marvelkmp.data.local.DatabaseDriverFactory

fun MainViewController() = ComposeUIViewController { App(DatabaseDriverFactory()) }