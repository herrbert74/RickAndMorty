package com.alvaroquintana.rickandmorty.domain.usecase

import com.alvaroquintana.rickandmorty.domain.api.CharacterRepository
import javax.inject.Inject

class GetAllFavoriteCharactersUseCase @Inject constructor(private val characterRepository: CharacterRepository) {
	suspend operator fun invoke() = characterRepository.getAllFavoriteCharacters()
}
