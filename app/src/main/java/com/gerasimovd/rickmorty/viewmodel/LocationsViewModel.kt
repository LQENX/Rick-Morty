package com.gerasimovd.rickmorty.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.gerasimovd.rickmorty.model.server.api.ApiService
import com.gerasimovd.rickmorty.paging.LocationsPagerSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class LocationsViewModel @Inject constructor(private val apiService: ApiService) : ViewModel() {

    val locationsData =
        Pager(PagingConfig(pageSize = 1)) { LocationsPagerSource(apiService) }
            .flow
            .cachedIn(viewModelScope)
}