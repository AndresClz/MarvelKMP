package com.example.marvelkmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform