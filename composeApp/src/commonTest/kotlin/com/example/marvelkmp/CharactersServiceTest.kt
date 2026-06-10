package com.example.marvelkmp

import com.example.marvelkmp.domain.Character
import com.example.marvelkmp.domain.CharactersRepository
import com.example.marvelkmp.domain.CharactersService
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CharactersServiceTest {

    private val mockCharacters = listOf(
        Character(id = 1009368, name = "Iron Fist", description = "Trained in the martial arts of K'un-Lun.", thumbnailUrl = ""),
        Character(id = 1009664, name = "Thor", description = "Norse God of thunder.", thumbnailUrl = ""),
        Character(id = 1017100, name = "Iron Man", description = "Genius. Billionaire.", thumbnailUrl = ""),
        Character(id = 1011490, name = "Rocket Raccoon", description = "", thumbnailUrl = ""),
        Character(id = 1010903, name = "Groot", description = "", thumbnailUrl = ""),
        Character(id = 1009562, name = "Scarlet Witch", description = "", thumbnailUrl = ""),
    )

    private val repository = object : CharactersRepository {
        override suspend fun getCharacters(): List<Character> = mockCharacters
    }

    private val service = CharactersService(repository)

    @Test
    fun ordenamiento_conDescripcion_ascendente_sinDescripcion_descendente() = runTest {
        val result = service.getOrderedCharacters()

        val expectedNames = listOf(
            "Iron Fist",      // id 1009368 — con desc, asc
            "Thor",           // id 1009664 — con desc, asc
            "Iron Man",       // id 1017100 — con desc, asc
            "Rocket Raccoon", // id 1011490 — sin desc, desc
            "Groot",          // id 1010903 — sin desc, desc
            "Scarlet Witch",  // id 1009562 — sin desc, desc
        )
        assertEquals(expectedNames, result.map { it.name })
    }

    @Test
    fun ordenamiento_primero_grupo_con_descripcion() = runTest {
        val result = service.getOrderedCharacters()

        val conDesc = result.takeWhile { it.description.isNotBlank() }
        val sinDesc = result.dropWhile { it.description.isNotBlank() }

        assertEquals(3, conDesc.size)
        assertEquals(3, sinDesc.size)
        sinDesc.forEach { assertEquals("", it.description) }
    }
}
