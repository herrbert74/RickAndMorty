package com.alvaroquintana.rickandmorty.data.network

import com.alvaroquintana.rickandmorty.domain.Character
import com.alvaroquintana.rickandmorty.domain.CharacterResult

data class CharacterResultDto(
	val info: Info,
	val results: List<CharacterDto>
)

data class CharacterDto(
	val id: Int,
	val name: String,
	val image: String,
	val gender: String,
	val status: String,
	val species: String,
	val type: String,
	val url: String,
	val created: String,
	val episode: ArrayList<String>,
	val location: DataObject,
	val origin: DataObject
)

data class Info(
	val count: Int,
	val pages: Int,
	val next: String?,
	val prev: String?
)

data class DataObject(
	val name: String,
	val url: String
)

fun CharacterResultDto.toCharacterResult(): CharacterResult = CharacterResult(
	com.alvaroquintana.rickandmorty.domain.Info(info.count, info.pages, info.next, info.prev),
	results.map {
		it.toCharacter()
	}
)

fun CharacterDto.toCharacter() = Character(
	id,
	name,
	image,
	gender,
	status,
	species,
	type,
	url,
	created,
	episode,
	com.alvaroquintana.rickandmorty.domain.DataObject(location.name, location.url),
	com.alvaroquintana.rickandmorty.domain.DataObject(origin.name, origin.url),
	false
)
