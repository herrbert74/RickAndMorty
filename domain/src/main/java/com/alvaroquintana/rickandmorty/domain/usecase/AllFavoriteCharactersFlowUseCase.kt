package com.alvaroquintana.rickandmorty.domain.usecase

import com.alvaroquintana.rickandmorty.domain.api.CharacterRepository
import javax.inject.Inject

class AllFavoriteCharactersFlowUseCase @Inject constructor(private val characterRepository: CharacterRepository) {
	operator fun invoke() = characterRepository.allFavoriteCharactersFlow()
}
