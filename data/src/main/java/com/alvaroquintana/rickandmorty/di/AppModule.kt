package com.alvaroquintana.rickandmorty.di

import android.content.Context
import androidx.room.Room
import com.alvaroquintana.rickandmorty.data.database.FavoriteCharacterDao
import com.alvaroquintana.rickandmorty.data.database.FavoriteDataBase
import com.alvaroquintana.rickandmorty.data.database.RoomDataSource
import com.alvaroquintana.rickandmorty.data.network.RickAndMortyDataSource
import com.alvaroquintana.rickandmorty.data.repository.CharacterAccessor
import com.alvaroquintana.rickandmorty.data.source.CharacterDataSource
import com.alvaroquintana.rickandmorty.data.source.LocalDataSource
import com.alvaroquintana.rickandmorty.domain.api.CharacterRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
	@Provides
	@Singleton
	fun provideDataBase(@ApplicationContext context: Context): FavoriteDataBase {
		return Room.databaseBuilder(
			context,
			FavoriteDataBase::class.java,
			"favorite-characters-db"
		).build()
	}

	@Provides
	@Singleton
	fun providesFavoriteCharacterDao(dataBase: FavoriteDataBase): FavoriteCharacterDao {
		return dataBase.favoriteCharacterDao()
	}

}

@Module
@InstallIn(SingletonComponent::class)
abstract class AppDataModule {

	@Binds
	abstract fun bindCharacterRepository(characterAccessor: CharacterAccessor): CharacterRepository

	@Binds
	abstract fun bindRemoteDataSource(remoteDataSource: RickAndMortyDataSource): CharacterDataSource

	@Binds
	abstract fun bindLocalDataSource(localDataSource: RoomDataSource): LocalDataSource

}
