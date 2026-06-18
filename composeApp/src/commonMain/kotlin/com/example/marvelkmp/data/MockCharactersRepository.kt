package com.example.marvelkmp.data

import com.example.marvelkmp.domain.Character
import com.example.marvelkmp.domain.CharactersRepository
import kotlinx.io.IOException

class MockCharactersRepository : CharactersRepository {

    var shouldFail: Boolean = false

    override suspend fun getCharacters(): List<Character> {
        if (shouldFail) {
            throw IOException("Simulando el error de Ktor")
        }

        return listOf(
            Character(
                id = 1009368,
                name = "Iron Fist",
                description = "Trained in the martial arts of K'un-Lun, Danny Rand returned to New York and became the Immortal Iron Fist.",
                thumbnailUrl = "http://i.annihil.us/u/prod/marvel/i/mg/3/f0/51424257af2f4/standard_fantastic.jpg",
            ),
            Character(
                id = 1009664,
                name = "Thor",
                description = "As the Norse God of thunder and lightning, Thor wields one of the greatest weapons ever made, the enchanted hammer Mjolnir.",
                thumbnailUrl = "http://i.annihil.us/u/prod/marvel/i/mg/7/10/537bc71e9286f/standard_fantastic.jpg",
            ),
            Character(
                id = 1017100,
                name = "Iron Man",
                description = "Genius. Billionaire. Playboy. Philanthropist. Tony Stark's got it all, and that's just the beginning.",
                thumbnailUrl = "http://i.annihil.us/u/prod/marvel/i/mg/9/c0/527bb7b37ff55/standard_fantastic.jpg",
            ),
            Character(
                id = 1011490,
                name = "Rocket Raccoon",
                description = "",
                thumbnailUrl = "http://i.annihil.us/u/prod/marvel/i/mg/9/b0/50fec1e49298a/standard_fantastic.jpg",
            ),
            Character(
                id = 1010903,
                name = "Groot",
                description = "",
                thumbnailUrl = "http://i.annihil.us/u/prod/marvel/i/mg/3/10/526033c8b474a/standard_fantastic.jpg",
            ),
            Character(
                id = 1009562,
                name = "Scarlet Witch",
                description = "",
                thumbnailUrl = "http://i.annihil.us/u/prod/marvel/i/mg/6/70/5261a7d7e607c/standard_fantastic.jpg",
            ),
        )
    }
}
