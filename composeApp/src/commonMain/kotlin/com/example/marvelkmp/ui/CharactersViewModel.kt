package com.example.marvelkmp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvelkmp.domain.Character
import com.example.marvelkmp.domain.CharactersService
import com.example.marvelkmp.domain.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CharactersViewModel(
    private val service: CharactersService,
) : ViewModel() {

    private val _state = MutableStateFlow<ScreenState>(ScreenState.Loading)
    val state: StateFlow<ScreenState> = _state.asStateFlow()

    private var cachedCharacters: List<Character>? = null

    init {
        load()
    }

    fun selectCharacter(character: Character) {
        _state.value = ScreenState.ShowDetail(character)
    }

    fun back() {
        val characters = cachedCharacters
        if (characters != null) {
            _state.value = ScreenState.ShowCharacters(characters)
        } else {
            load()
        }
    }

    private fun load() = viewModelScope.launch {
        _state.value = ScreenState.Loading
        try {
            val characters = service.getOrderedCharacters()
            cachedCharacters = characters
            _state.value = ScreenState.ShowCharacters(characters)
        } catch (e: Exception) {
            println("CharactersViewModel: error loading characters: $e")
        }
    }
}
