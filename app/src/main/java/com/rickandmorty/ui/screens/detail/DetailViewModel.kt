package com.rickandmorty.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rickandmorty.data.Error
import com.rickandmorty.domain.Character
import com.rickandmorty.usecases.GetCharacterByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getCharacterByIdUseCase: GetCharacterByIdUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    fun findCharacter(characterId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }

            val characterResponse = getCharacterByIdUseCase(characterId)
            characterResponse.fold(
                { exception ->
                    _state.update { it.copy(error = exception) }
                }, { character ->
                    _state.update { UiState(character = character) }
                })

            _state.update { it.copy(loading = false) }
        }
    }

    data class UiState(
        val loading: Boolean = false,
        val character: Character? = null,
        val error: Error? = null
    )
}