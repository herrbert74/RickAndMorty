package com.alvaroquintana.rickandmorty.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [CharacterDbo::class], version = 1, exportSchema = false)
@TypeConverters(EpisodeConverter::class)
abstract class FavoriteDataBase : RoomDatabase() {

	abstract fun favoriteCharacterDao(): FavoriteCharacterDao
}
