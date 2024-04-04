package com.alvaroquintana.rickandmorty.data.repository

import com.alvaroquintana.common.ext.ApiResult
import com.alvaroquintana.common.ext.apiRunCatching
import com.alvaroquintana.rickandmorty.data.source.CharacterDataSource
import com.alvaroquintana.rickandmorty.data.source.LocalDataSource
import com.alvaroquintana.rickandmorty.domain.Character
import com.alvaroquintana.rickandmorty.domain.CharacterResult
import javax.inject.Inject

class CharacterRepository @Inject constructor(
	private val characterDataSource: CharacterDataSource,
	private val localDataSource: LocalDataSource
) {

	fun allFavoriteCharactersFlow() =
		localDataSource.allFavoritesFlow()

	suspend fun getAllFavoriteCharacters() =
		localDataSource.getAllFavoriteCharacters()

	suspend fun insertFavoriteCharacter(id: Character) =
		localDataSource.insertFavoriteCharacter(id)

	suspend fun deleteFavoriteCharacter(id: Character) =
		localDataSource.deleteFavoriteCharacter(id)

	suspend fun getCharacters(
		page: Int,
		nameFiltered: String?,
		statusFiltered: String?,
		genderFiltered: String?
	): ApiResult<CharacterResult> {
		return apiRunCatching {
			val favoritesList = localDataSource.getAllFavoriteCharacters()
			val characterList = characterDataSource.getCharacters(
				page = page,
				nameFiltered = nameFiltered,
				genderFiltered = genderFiltered,
				statusFiltered = statusFiltered
			)

			val charactersListFinal = characterList.results.mapWithFavoriteList(favoritesList)
			CharacterResult(characterList.info, charactersListFinal)
		}
	}

	suspend fun getCharacterById(id: Int): ApiResult<Character> {
		return apiRunCatching {
			val character = characterDataSource.getCharacterById(id)
			val isFavorite = localDataSource.isFavoriteCharacterById(id)

			character.copy(favorite = isFavorite)
		}
	}

	private fun (List<Character>).mapWithFavoriteList(favoritesList: List<Character>): List<Character> {
		val resultList = map { serverCharacter ->
			if (favoritesList.find { it.id == serverCharacter.id } != null) {
				serverCharacter.copy(favorite = true)
			} else {
				serverCharacter.copy(favorite = false)
			}
		}
		return resultList
	}
}
