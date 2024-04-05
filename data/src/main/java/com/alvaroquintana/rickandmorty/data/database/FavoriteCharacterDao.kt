package com.alvaroquintana.rickandmorty.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteCharacterDao {

    @Query("SELECT COUNT(id) FROM Favorite")
    suspend fun favoriteCount(): Int

    @Query("SELECT * FROM Favorite")
    fun favoriteListFlow(): Flow<List<CharacterDbo>>

    @Query("SELECT * FROM Favorite")
    suspend fun favoriteCharactersList(): List<CharacterDbo>

    @Query("SELECT * FROM Favorite WHERE id = :idCharacter LIMIT 1")
    suspend fun isFavoriteCharacters(idCharacter: Int): CharacterDbo?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavoriteCharacter(favoriteCharacter: CharacterDbo)

    @Delete
    suspend fun deleteFavoriteCharacter(favoriteCharacter: CharacterDbo)
}