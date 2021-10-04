package com.gerasimovd.rickmorty.viewmodel.episode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.gerasimovd.rickmorty.model.entities.character.Character
import com.gerasimovd.rickmorty.model.repository.RickMortyRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@HiltViewModel
class EpisodeInfoViewModel @ExperimentalPagingApi
@Inject constructor(private val repository: RickMortyRepo) : ViewModel() {

    @ExperimentalPagingApi
    fun getEpisodeById(episodeId: Int) =
        repository.getEpisodeById(episodeId)

    @ExperimentalPagingApi
    fun getCharacters(): Flow<PagingData<Character>> =
        repository.getCharactersFlow().cachedIn(viewModelScope)
}