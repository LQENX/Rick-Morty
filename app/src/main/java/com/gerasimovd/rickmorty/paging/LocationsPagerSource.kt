package com.gerasimovd.rickmorty.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gerasimovd.rickmorty.model.server.api.ApiService
import com.gerasimovd.rickmorty.model.server.dto.character.CharacterDto
import com.gerasimovd.rickmorty.model.server.dto.location.LocationDto


class LocationsPagerSource(private val apiService: ApiService) : PagingSource<Int, LocationDto>() {
    private val TAG: String? = LocationsPagerSource::class.java.simpleName


    override fun getRefreshKey(state: PagingState<Int, LocationDto>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LocationDto> {
        val currentPage = params.key ?: 1

        return try {
            val response = apiService.getLocations(currentPage)
            val responseLocations = mutableListOf<LocationDto>()
                .apply { addAll(response.body()?.locations ?: emptyList()) }
            LoadResult.Page(
                data = responseLocations,
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