package com.gerasimovd.rickmorty.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gerasimovd.rickmorty.model.server.api.ApiService
import com.gerasimovd.rickmorty.model.server.dto.character.CharacterDto


class CharactersPagingSource(private val apiService: ApiService) : PagingSource<Int, CharacterDto>() {
    private val TAG: String? = CharactersPagingSource::class.java.simpleName

    override fun getRefreshKey(state: PagingState<Int, CharacterDto>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterDto> {
        /*lateinit var result: LoadResult<Int, CharacterResponse>*/
        val currentPage = params.key ?: 1
        return try {
            val response = apiService.getCharacters(currentPage)
            val responseCharacters = mutableListOf<CharacterDto>()
                .apply { addAll(response.body()?.characters ?: emptyList()) }
            LoadResult.Page(
                data = responseCharacters,
                prevKey = if (currentPage == 1) null else -1,
                nextKey = currentPage.plus(1)
            )

        } catch (e: Exception) {
            Log.d(TAG, "${e.printStackTrace()}")
            LoadResult.Error(e)
        }
        /*apiService.getCharacters(currentPage)
            .onSuccess {
                val responseCharacters = mutableListOf<CharacterResponse>().apply { addAll(it) }
                result = LoadResult.Page(
                    data = responseCharacters,
                    prevKey = if (currentPage == 1) null else -1,
                    nextKey = currentPage.plus(1)
                )
            }
            .onFailure { result = LoadResult.Error(it) }*/

        /*return result*/
    }
}