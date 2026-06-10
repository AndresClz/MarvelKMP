package com.example.marvelkmp.domain

interface CharactersRepository {
    suspend fun getCharacters(): List<Character>
}
