package com.alvaroquintana.rickandmorty.di

import android.content.Context
import androidx.room.Room
import com.alvaroquintana.data.BuildConfig
import com.alvaroquintana.rickandmorty.data.database.FavoriteCharacterDao
import com.alvaroquintana.rickandmorty.data.database.FavoriteDataBase
import com.alvaroquintana.rickandmorty.data.database.RoomDataSource
import com.alvaroquintana.rickandmorty.data.network.RickAndMortyService
import com.alvaroquintana.rickandmorty.data.repository.CharacterAccessor
import com.alvaroquintana.rickandmorty.data.source.LocalDataSource
import com.alvaroquintana.rickandmorty.domain.api.CharacterRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "https://rickandmortyapi.com/api/"

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
	fun provideFavoriteCharacterDao(dataBase: FavoriteDataBase): FavoriteCharacterDao {
		return dataBase.favoriteCharacterDao()
	}

	@Provides
	@Singleton
	fun provideRickAndMortyService(): RickAndMortyService {
		return Retrofit.Builder()
			.baseUrl(BASE_URL)
			.client(okHttpClient)
			.addConverterFactory(GsonConverterFactory.create())
			.build()
			.create(RickAndMortyService::class.java)
	}

	private val okHttpClient = HttpLoggingInterceptor().run {
		if (BuildConfig.DEBUG) level = HttpLoggingInterceptor.Level.BODY
		OkHttpClient.Builder().addInterceptor(this).build()
	}

}

@Module
@InstallIn(SingletonComponent::class)
abstract class AppDataModule {

	@Binds
	abstract fun bindCharacterRepository(characterAccessor: CharacterAccessor): CharacterRepository

	@Binds
	abstract fun bindLocalDataSource(localDataSource: RoomDataSource): LocalDataSource

}
