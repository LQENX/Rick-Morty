package com.gerasimovd.rickmorty.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.room.Room
import com.gerasimovd.rickmorty.model.database.AppDatabase
import com.gerasimovd.rickmorty.model.remote.api.ApiService
import com.gerasimovd.rickmorty.repository.RickMortyRepo
import com.gerasimovd.rickmorty.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideBaseUrl() = Constants.BASE_URL

    @Provides
    fun provideReadTimeout() = Constants.READ_TIMEOUT

    @Provides
    @Singleton
    fun provideRetrofitInstance(BASE_URL: String, READ_TIMEOUT: Long): ApiService {
        val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabaseInstance(@ApplicationContext applicationContext: Context): AppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "AppDatabase")
            .build()
    }

    @ExperimentalPagingApi
    @Provides
    @Singleton
    fun provideRickMortyRepo(apiService: ApiService, database: AppDatabase): RickMortyRepo {
        return RickMortyRepo.newInstance(apiService, database)
    }
}