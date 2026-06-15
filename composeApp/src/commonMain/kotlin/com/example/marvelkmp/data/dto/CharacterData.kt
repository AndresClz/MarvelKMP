package com.example.marvelkmp.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CharacterData(
    val results: List<CharacterResult>,
)
