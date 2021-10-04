package com.gerasimovd.rickmorty.viewmodel.character

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
class CharactersViewModel @ExperimentalPagingApi
@Inject constructor(private val repository: RickMortyRepo) : ViewModel() {

    /** Get characters only from Room (by name) */
    @ExperimentalPagingApi
    fun getCharacters(characterName: String = ""): Flow<PagingData<Character>> =
        repository.getCharactersFlow(characterName).cachedIn(viewModelScope)

}