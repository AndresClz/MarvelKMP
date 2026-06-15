package com.example.marvelkmp.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CharactersResponse(
    val code: Int,
    val status: String,
    val data: CharacterData,
)