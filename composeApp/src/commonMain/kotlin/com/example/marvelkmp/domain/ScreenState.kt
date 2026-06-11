package com.example.marvelkmp.domain

sealed interface ScreenState {
    data object Loading : ScreenState
    data class ShowCharacters(val characters: List<Character>) : ScreenState
}
