package com.alvaroquintana.rickandmorty.domain.usecase

import com.alvaroquintana.rickandmorty.domain.api.CharacterRepository
import javax.inject.Inject

class GetCharacterByIdUseCase @Inject constructor(private val characterRepository: CharacterRepository) {
	suspend operator fun invoke(id: Int) = characterRepository.getCharacterById(id)
}
