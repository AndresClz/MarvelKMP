package com.example.marvelkmp.domain

class CharactersService(private val repository: CharactersRepository) {
    suspend fun getOrderedCharacters(): List<Character> {
        val all = repository.getCharacters()
        val withDesc = all.filter { it.description.isNotBlank() }.sortedBy { it.id }
        val withoutDesc = all.filter { it.description.isBlank() }.sortedByDescending { it.id }
        return withDesc + withoutDesc
    }
}
