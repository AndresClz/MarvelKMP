package com.example.marvelkmp.data.repositories

import com.example.marvelkmp.data.Constants
import com.example.marvelkmp.data.crypto.currentTimeMillis
import com.example.marvelkmp.data.crypto.md5
import com.example.marvelkmp.data.dto.CharactersResponse
import com.example.marvelkmp.domain.Character
import com.example.marvelkmp.domain.CharactersRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class KtorCharactersRepository(
    private val client: HttpClient,
) : CharactersRepository {
    override suspend fun getCharacters(): List<Character> {
        val timestamp = currentTimeMillis().toString()
        val hash = md5(timestamp + Constants.PRIVATE_KEY + Constants.PUBLIC_KEY)

        val response: CharactersResponse =
            client
                .get(Constants.CHARACTERS_URL) {
                    parameter("ts", timestamp)
                    parameter("apikey", Constants.PUBLIC_KEY)
                    parameter("hash", hash)
                }
                .body<CharactersResponse>()

        return response.data.results.map { dto ->
            Character(
                id = dto.id,
                name = dto.name,
                description = dto.description,
                thumbnailUrl = if (dto.thumbnail.extension.isNotEmpty()) "${dto.thumbnail.path}.${dto.thumbnail.extension}" else dto.thumbnail.path,
            )
        }
    }
}
