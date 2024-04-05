package com.alvaroquintana.rickandmorty.data.repository

import com.alvaroquintana.common.ext.ApiResult
import com.alvaroquintana.common.ext.apiRunCatching
import com.alvaroquintana.rickandmorty.data.network.RickAndMortyService
import com.alvaroquintana.rickandmorty.data.source.LocalDataSource
import com.alvaroquintana.rickandmorty.domain.Character
import com.alvaroquintana.rickandmorty.domain.CharacterResult
import com.alvaroquintana.rickandmorty.domain.api.CharacterRepository
import javax.inject.Inject

class CharacterAccessor @Inject constructor(
	private val rickAndMortyService: RickAndMortyService,
	private val localDataSource: LocalDataSource
) : CharacterRepository {

	override fun allFavoriteCharactersFlow() = localDataSource.allFavoritesFlow()

	override suspend fun getAllFavoriteCharacters() = localDataSource.getAllFavoriteCharacters()

	override suspend fun insertFavoriteCharacter(id: Character) = localDataSource.insertFavoriteCharacter(id)

	override suspend fun deleteFavoriteCharacter(id: Character) = localDataSource.deleteFavoriteCharacter(id)

	override suspend fun getCharacters(
		page: Int,
		nameFiltered: String?,
		statusFiltered: String?,
		genderFiltered: String?
	): ApiResult<CharacterResult> {
		return apiRunCatching {
			val favoritesList = localDataSource.getAllFavoriteCharacters()
			val characterList = rickAndMortyService.getCharactersAsync(
				page = page,
				nameFiltered = nameFiltered,
				genderFiltered = genderFiltered,
				statusFiltered = statusFiltered
			)

			val charactersListFinal = characterList.results.mapWithFavoriteList(favoritesList)
			CharacterResult(characterList.info, charactersListFinal)
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

	override suspend fun getCharacterById(id: Int): ApiResult<Character> {
		return apiRunCatching {
			val character = rickAndMortyService.getCharacterByIdAsync(id)
			val isFavorite = localDataSource.isFavoriteCharacterById(id)
			character.copy(favorite = isFavorite)
		}
	}

}
