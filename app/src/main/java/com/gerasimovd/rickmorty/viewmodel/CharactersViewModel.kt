package com.gerasimovd.rickmorty.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.gerasimovd.rickmorty.model.server.api.ApiService
import com.gerasimovd.rickmorty.paging.CharactersPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class CharactersViewModel @Inject constructor(private val apiService: ApiService) : ViewModel() {

    val charactersData =
        Pager(PagingConfig(pageSize = 1)) { CharactersPagingSource(apiService) }
            .flow
            .cachedIn(viewModelScope)
}