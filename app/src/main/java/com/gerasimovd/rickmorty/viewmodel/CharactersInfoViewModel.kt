package com.gerasimovd.rickmorty.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.gerasimovd.rickmorty.model.entities.episode.Episode
import com.gerasimovd.rickmorty.repository.RickMortyRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@HiltViewModel
class CharactersInfoViewModel @ExperimentalPagingApi
@Inject constructor(private val repository: RickMortyRepo) : ViewModel() {

    @ExperimentalPagingApi
    fun getEpisodeById(episodeId: Int) =
        repository.getEpisodeById(episodeId)

    @ExperimentalPagingApi
    fun getCharacterById(characterId: Int) =
        repository.getCharacterById(characterId)

    @ExperimentalPagingApi
    fun getEpisodes(): Flow<PagingData<Episode>> =
        repository.getEpisodesFlow().cachedIn(viewModelScope)

    @ExperimentalPagingApi
    suspend fun insertEpisodes(episodes: List<Episode>) {
        repository.insertEpisodes(episodes)
    }
}