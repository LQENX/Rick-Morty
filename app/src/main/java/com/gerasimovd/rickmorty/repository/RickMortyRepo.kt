package com.gerasimovd.rickmorty.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.gerasimovd.rickmorty.model.database.AppDatabase
import com.gerasimovd.rickmorty.model.entities.Character
import com.gerasimovd.rickmorty.model.remote.api.ApiService
import com.gerasimovd.rickmorty.paging.CharacterMediator
import kotlinx.coroutines.flow.Flow


@ExperimentalPagingApi
class RickMortyRepo private constructor(
    private val apiService: ApiService,
    private val database: AppDatabase
) {

    companion object {
        fun newInstance(apiService: ApiService, database: AppDatabase) =
            RickMortyRepo(apiService, database)
    }

    fun getCharactersFlow(): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(pageSize = 1, prefetchDistance = 4),
            pagingSourceFactory = { database.getCharacterDao().getAllCharacters() },
            remoteMediator = CharacterMediator(apiService, database)
        ).flow
    }
}