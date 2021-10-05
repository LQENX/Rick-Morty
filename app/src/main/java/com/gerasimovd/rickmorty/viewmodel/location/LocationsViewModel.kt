package com.gerasimovd.rickmorty.viewmodel.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.gerasimovd.rickmorty.model.entities.location.Location
import com.gerasimovd.rickmorty.model.repository.RickMortyRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@HiltViewModel
class LocationsViewModel @ExperimentalPagingApi
@Inject constructor(private val repository: RickMortyRepo) : ViewModel() {

    /** Get episodes only from Room (by name) */
    @ExperimentalPagingApi
    fun getLocations(locationName: String = ""): Flow<PagingData<Location>> =
        repository.getLocationsFlow(locationName).cachedIn(viewModelScope)

}