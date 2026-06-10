package com.example.marvelkmp.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging

object HttpClientFactory {
    fun create(): HttpClient = HttpClient(httpClientEngine()) {
        install(Logging) { level = LogLevel.INFO }
        // ContentNegotiation(Json) → lo agrega Int. 2
    }
}

expect fun httpClientEngine(): HttpClientEngineFactory<*>
