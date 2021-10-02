package com.gerasimovd.rickmorty.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import com.gerasimovd.rickmorty.model.database.AppDatabase
import com.gerasimovd.rickmorty.model.remote.api.ApiService
import com.gerasimovd.rickmorty.repository.RickMortyRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class CharactersViewModel @Inject constructor(private val apiService: ApiService, private val database: AppDatabase) : ViewModel() {

    @ExperimentalPagingApi
    private val repository = RickMortyRepo.newInstance(apiService, database)

    @ExperimentalPagingApi
    val charactersMediatorData = repository.getCharactersFlow().cachedIn(viewModelScope)

}