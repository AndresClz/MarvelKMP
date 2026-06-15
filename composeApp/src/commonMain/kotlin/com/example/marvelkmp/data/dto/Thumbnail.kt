package com.example.marvelkmp.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class Thumbnail(
    val path: String,
    val extension: String,
)