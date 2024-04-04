package com.alvaroquintana.rickandmorty.domain.usecase

import com.alvaroquintana.rickandmorty.domain.Character
import com.alvaroquintana.rickandmorty.domain.api.CharacterRepository
import javax.inject.Inject

class UpdateFavoriteCharacterUseCase @Inject constructor(private val characterRepository: CharacterRepository) {
	suspend operator fun invoke(isFavorite: Boolean, character: Character) {
		if (isFavorite) characterRepository.insertFavoriteCharacter(character.copy(favorite = true))
		else characterRepository.deleteFavoriteCharacter(character)
	}
}
