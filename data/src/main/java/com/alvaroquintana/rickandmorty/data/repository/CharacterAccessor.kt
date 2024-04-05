package com.alvaroquintana.rickandmorty.data.repository

import com.alvaroquintana.common.ext.ApiResult
import com.alvaroquintana.common.ext.apiRunCatching
import com.alvaroquintana.rickandmorty.data.database.FavoriteCharacterDao
import com.alvaroquintana.rickandmorty.data.network.RickAndMortyService
import com.alvaroquintana.rickandmorty.data.toDomainCharacter
import com.alvaroquintana.rickandmorty.data.toRoomCharacter
import com.alvaroquintana.rickandmorty.domain.Character
import com.alvaroquintana.rickandmorty.domain.CharacterResult
import com.alvaroquintana.rickandmorty.domain.api.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CharacterAccessor @Inject constructor(
	private val rickAndMortyService: RickAndMortyService,
	private val favoriteCharacterDao: FavoriteCharacterDao
) : CharacterRepository {

	override fun allFavoriteCharactersFlow(): Flow<List<Character>> = favoriteCharacterDao.favoriteListFlow()
		.map { characterListDB ->
			characterListDB.map { it.toDomainCharacter() }
		}

	override suspend fun getAllFavoriteCharacters(): List<Character> = favoriteCharacterDao.favoriteCharactersList()
		.map { characterListDB ->
			characterListDB.toDomainCharacter()
		}

	override suspend fun insertFavoriteCharacter(character: Character) {
		favoriteCharacterDao.insertFavoriteCharacter(character.toRoomCharacter())
	}

	override suspend fun deleteFavoriteCharacter(character: Character) {
		favoriteCharacterDao.deleteFavoriteCharacter(character.toRoomCharacter())
	}

	override suspend fun getCharacters(
		page: Int,
		nameFiltered: String?,
		statusFiltered: String?,
		genderFiltered: String?
	): ApiResult<CharacterResult> {
		return apiRunCatching {
			val favoritesList = getAllFavoriteCharacters()
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
			val isFavorite = favoriteCharacterDao.isFavoriteCharacters(id) != null
			character.copy(favorite = isFavorite)
		}
	}

}
