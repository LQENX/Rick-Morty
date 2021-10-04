package com.gerasimovd.rickmorty.viewmodel

import androidx.lifecycle.ViewModel
import com.gerasimovd.rickmorty.model.remote.api.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class LocationsViewModel @Inject constructor(private val apiService: ApiService) : ViewModel() {

}