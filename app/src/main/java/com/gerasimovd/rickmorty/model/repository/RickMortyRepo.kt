package com.gerasimovd.rickmorty.model.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.gerasimovd.rickmorty.model.database.AppDatabase
import com.gerasimovd.rickmorty.model.entities.character.Character
import com.gerasimovd.rickmorty.model.entities.episode.Episode
import com.gerasimovd.rickmorty.model.remote.api.ApiService
import com.gerasimovd.rickmorty.paging.CharacterMediator
import com.gerasimovd.rickmorty.paging.EpisodeMediator
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

    fun getCharactersFlow(characterName: String = ""): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(pageSize = 1, prefetchDistance = 4),
            pagingSourceFactory = {
                if (characterName == "") database.getCharacterDao().getAllCharacters()
                else database.getCharacterDao().getCharactersByName(characterName) },
            remoteMediator =
            if (characterName == "") CharacterMediator(apiService, database)
            else CharacterMediator(apiService, database, isSearchMode = true)
        ).flow
    }

    fun getEpisodesFlow(episodesName: String = ""): Flow<PagingData<Episode>> {
        return Pager(
            config = PagingConfig(pageSize = 1, prefetchDistance = 4),
            pagingSourceFactory = {
                if (episodesName == "") database.getEpisodeDao().getAllEpisodes()
                else database.getEpisodeDao().getEpisodesByName(episodesName) },
            remoteMediator =
            if (episodesName == "") EpisodeMediator(apiService, database)
            else EpisodeMediator(apiService, database, isSearchMode = true)
        ).flow
    }

    fun getCharacterById(characterId: Int): Flow<Character> =
        database.getCharacterDao().getCharacterById(characterId)

    fun getEpisodeById(episodeId: Int): Flow<Episode> =
        database.getEpisodeDao().getEpisodeById(episodeId)

    suspend fun insertEpisodes(episodes: List<Episode>) {
        database.getEpisodeDao().insertEpisodes(episodes = episodes)
    }


}