package com.example.marvelkmp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvelkmp.domain.Character
import com.example.marvelkmp.domain.CharactersService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface HomeState {
    data object Loading : HomeState
    data class Success(val characters: List<Character>) : HomeState
    data class Error(val message: String) : HomeState
}

class HomeViewModel(
    private val service: CharactersService,
) : ViewModel() {

    private val _state = MutableStateFlow<HomeState>(HomeState.Loading)
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        load()
    }

    private fun load() = viewModelScope.launch {
        _state.value = HomeState.Loading
        try {
            _state.value = HomeState.Success(service.getOrderedCharacters())
        } catch (e: Exception) {
            println("HomeViewModel: error loading characters: $e")
            _state.value = HomeState.Error(e.message ?: "Unknown error")
        }
    }
}
