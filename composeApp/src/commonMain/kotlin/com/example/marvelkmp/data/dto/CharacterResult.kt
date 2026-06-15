package com.example.marvelkmp.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CharacterResult(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: Thumbnail,
)