package com.gerasimovd.rickmorty.viewmodel.location

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
class LocationInfoViewModel @ExperimentalPagingApi
@Inject constructor(private val repository: RickMortyRepo) : ViewModel() {

    @ExperimentalPagingApi
    fun getLocationById(locationId: Int) =
        repository.getLocationById(locationId)

    @ExperimentalPagingApi
    fun getCharacters(): Flow<PagingData<Character>> =
        repository.getCharactersFlow().cachedIn(viewModelScope)
}