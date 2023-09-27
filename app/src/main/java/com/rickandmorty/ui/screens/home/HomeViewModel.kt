package com.rickandmorty.ui.screens.home

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rickandmorty.data.Error
import com.rickandmorty.domain.Character
import com.rickandmorty.usecases.CharacterUseCase
import com.rickandmorty.usecases.FavoriteCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val favoriteCharactersUseCase: FavoriteCharactersUseCase,
    private val characterUseCase: CharacterUseCase
) : ViewModel() {
    var nameFilter: String? = null
    var genderFilter: String? = null
    var statusFilter: String? = null
    var selectedTabIndex: Int = 0

    private var totalPages: Int = 1
    var nextPage: Int = 1

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    private val _favState = MutableStateFlow<List<Character>>(emptyList())
    val favState: StateFlow<List<Character>> = _favState.asStateFlow()

    init {
        findCharacters()
    }

    fun findCharacters() {
        if (selectedTabIndex == 0) {
            // Server characters
            viewModelScope.launch {
                _state.update { it.copy(loading = true) }
                requestCharacters()
                _state.update { it.copy(loading = false) }
            }
        } else {
            viewModelScope.launch {
                _state.update { it.copy(loading = true) }
                favoriteCharactersUseCase.favoriteCharacters().collect { favoritesList ->
                    _favState.value = favoriteListFiltered(favoritesList)
                }
                _state.update { it.copy(loading = false) }
            }
        }
    }

    private fun favoriteListFiltered(favoritesList: List<Character>): List<Character> {
        return favoritesList.filter { character ->
            (nameFilter == null || character.name.contains(
                nameFilter.toString(), ignoreCase = true
            )) && (genderFilter == null || character.gender.equals(
                genderFilter,
                ignoreCase = true
            )) && (statusFilter == null || character.status.equals(
                statusFilter,
                ignoreCase = true
            ))
        }
    }

    fun updateList() {
        if (nextPage < totalPages) {
            findCharacters()
        } else {
            _state.update { it.copy(noMoreItemFound = true) }
        }
    }

    fun cleanList() {
        _state.update { it.copy(characterList = emptyList()) }
    }

    private suspend fun requestCharacters() {
        val characterListResponse = characterUseCase.getCharacters(
            page = nextPage,
            nameFiltered = nameFilter,
            genderFiltered = genderFilter,
            statusFiltered = statusFilter
        )

        characterListResponse.fold({ exception ->
            _state.update { it.copy(error = exception) }
        }, { characterList ->
            _state.update {
                it.copy(
                    characterList = (_state.value.characterList
                        ?: emptyList()).plus(characterList.results)
                )
            }

            if (characterList.info.next != null) {
                val uri: Uri = Uri.parse(characterList.info.next)
                nextPage = uri.getQueryParameter(PAGE)?.toInt() ?: 0
                totalPages = characterList.info.pages
                _state.update { it.copy(noMoreItemFound = false) }
            } else {
                nextPage = totalPages
                _state.update { it.copy(noMoreItemFound = true) }
            }
        })

        characterListWithFavorites()
    }

    fun saveFavorite(isFavorite: Boolean, character: Character) {
        viewModelScope.launch {
            if (isFavorite) {
                favoriteCharactersUseCase.deleteFavoriteCharacter(character)
            } else {
                favoriteCharactersUseCase.insertFavoriteCharacter(character.copy(favorite = true))
            }
        }
    }

    private suspend fun characterListWithFavorites() {
        favoriteCharactersUseCase.favoriteCharacters().collect { favoritesList ->
            _favState.value = favoritesList

            val finalList = (_state.value.characterList ?: emptyList()).map { serverCharacter ->
                if (favoritesList.find { it.id == serverCharacter.id } != null) {
                    serverCharacter.copy(favorite = true)
                } else {
                    serverCharacter.copy(favorite = false)
                }
            }

            _state.update { UiState(characterList = finalList) }
        }
    }

    data class UiState(
        val loading: Boolean = false,
        val characterList: List<Character>? = null,
        val noMoreItemFound: Boolean = false,
        val error: Error? = null
    )

    companion object {
        const val PAGE = "page"
    }
}