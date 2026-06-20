package com.example.marvelkmp.data.repositories

import com.example.marvelkmp.data.Constants
import com.example.marvelkmp.data.dto.CharactersResponse
import com.example.marvelkmp.domain.Character
import com.example.marvelkmp.domain.CharactersRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class KtorCharactersRepository(
    private val client: HttpClient,
) : CharactersRepository {
    override suspend fun getCharacters(): List<Character> {
        val response: CharactersResponse =
            client
                .get(Constants.CHARACTERS_URL)
                .body<CharactersResponse>()

        return response.data.results.map { dto ->
            Character(
                id = dto.id,
                name = dto.name,
                description = dto.description,
                thumbnailUrl = if (dto.thumbnail.extension.isNotEmpty()) "${dto.thumbnail.path}.${dto.thumbnail.extension}" else dto.thumbnail.path
            )
        }
    }
}
