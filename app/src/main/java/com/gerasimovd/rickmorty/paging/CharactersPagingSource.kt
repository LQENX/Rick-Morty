package com.gerasimovd.rickmorty.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gerasimovd.rickmorty.model.server.api.ApiService
import com.gerasimovd.rickmorty.model.server.dto.character.CharacterDto
import com.gerasimovd.rickmorty.model.server.entities.CharacterEntity
import com.gerasimovd.rickmorty.model.server.entities.EpisodeEntity


class CharactersPagingSource(private val apiService: ApiService) : PagingSource<Int, CharacterDto>() {
    private val TAG: String? = CharactersPagingSource::class.java.simpleName


    override fun getRefreshKey(state: PagingState<Int, CharacterDto>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterDto> {
        val currentPage = params.key ?: 1

        return try {
            val responseCharacters = apiService.getCharacters(currentPage)

            val characters = mutableListOf<CharacterDto>()
                .apply { addAll(responseCharacters.body()?.characters ?: emptyList()) }
            LoadResult.Page(
                data = characters,
                prevKey = if (currentPage == 1) null else -1,
                nextKey = currentPage.plus(1)
            )

        } catch (e: Exception) {
            Log.d(TAG, "Error on load")
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }
}