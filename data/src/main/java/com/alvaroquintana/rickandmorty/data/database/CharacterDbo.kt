package com.alvaroquintana.rickandmorty.data.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.alvaroquintana.rickandmorty.domain.Character
import com.alvaroquintana.rickandmorty.domain.DataObject
import com.google.gson.Gson

@Entity(tableName = "Favorite")
data class CharacterDbo(
	@PrimaryKey(autoGenerate = true) val id: Int,
	val name: String,
	val image: String,
	val gender: String,
	val status: String,
	val species: String,
	val type: String,
	val url: String,
	val created: String,
	@field:TypeConverters(EpisodeConverter::class) val episode: EpisodeList,
	@Embedded(prefix = "location_") val location: DataObjectDbo,
	@Embedded(prefix = "origin_") val origin: DataObjectDbo,
	val favorite: Boolean = true
)

data class EpisodeList(
	val episodeList: ArrayList<String>
)

data class DataObjectDbo(
	val name: String,
	val url: String
)

class EpisodeConverter {

	@TypeConverter
	fun fromEpisode(value: EpisodeList?): String {
		return Gson().toJson(value ?: EpisodeList(ArrayList()))
	}

	@TypeConverter
	fun toEpisode(value: String): EpisodeList {
		return Gson().fromJson(value, EpisodeList::class.java)
	}
}

fun Character.toDbo(): CharacterDbo = CharacterDbo(
	id,
	name,
	image,
	gender,
	status,
	species,
	type,
	url,
	created,
	EpisodeList(episode),
	DataObjectDbo(location.name, location.url),
	DataObjectDbo(origin.name, origin.url),
	favorite
)

fun CharacterDbo.toCharacter(): Character = Character(
	id,
	name,
	image,
	gender,
	status,
	species,
	type,
	url,
	created,
	episode.episodeList,
	DataObject(location.name, location.url),
	DataObject(origin.name, origin.url),
	favorite
)
