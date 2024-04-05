package com.alvaroquintana.rickandmorty.domain.api

import com.alvaroquintana.common.ext.ApiResult
import com.alvaroquintana.rickandmorty.domain.Character
import com.alvaroquintana.rickandmorty.domain.CharacterResult
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
	fun allFavoriteCharactersFlow(): Flow<List<Character>>

	suspend fun getAllFavoriteCharacters(): List<Character>

	suspend fun insertFavoriteCharacter(character: Character)

	suspend fun deleteFavoriteCharacter(character: Character)

	suspend fun getCharacters(
		page: Int,
		nameFiltered: String?,
		statusFiltered: String?,
		genderFiltered: String?
	): ApiResult<CharacterResult>

	suspend fun getCharacterById(id: Int): ApiResult<Character>

}
