package com.example.marvelkmp.data.local

import com.example.marvelkmp.cache.CharacterEntity
import com.example.marvelkmp.cache.MarvelDatabase
import com.example.marvelkmp.domain.Character
import com.example.marvelkmp.domain.CharactersRepository
import kotlin.coroutines.cancellation.CancellationException

class CacheCharactersRepository(
    private val apiRepository: CharactersRepository,
    database: MarvelDatabase
) : CharactersRepository {

    private val queries = database.characterQueries

    override suspend fun getCharacters(): List<Character> {
        return try {
            // 1. Intentar obtener personajes de la red
            val remoteCharacters = apiRepository.getCharacters()

            // 2. Si la llamada es exitosa -> guardar en la base local
            queries.transaction {
                queries.clearCharacters()
                remoteCharacters.forEach { character ->
                    queries.insertCharacter(
                        id = character.id.toLong(), // SQLDelight usa Long por defecto para INTEGER
                        name = character.name,
                        description = character.description,
                        thumbnailUrl = character.thumbnailUrl
                    )
                }
            }
            remoteCharacters
        } catch (e: Exception) {
            if (e is CancellationException) throw e

            // 3. Si la llamada falla -> devolver lo que haya en la base local
            val localCharacters = queries.selectAllCharacters()
                .executeAsList()
                .map { it.toDomain() }

            // 4. Si la base local también está vacía -> propagar el error
            if (localCharacters.isEmpty()) {
                throw e
            }

            localCharacters
        }
    }
}

// Mapper de la entidad de base de datos al modelo de dominio
fun CharacterEntity.toDomain(): Character {
    return Character(
        id = this.id.toInt(), // Convertimos el Long de la BD al Int del dominio
        name = this.name,
        description = this.description,
        thumbnailUrl = this.thumbnailUrl
    )
}