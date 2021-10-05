package com.gerasimovd.rickmorty.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.gerasimovd.rickmorty.model.database.AppDatabase
import com.gerasimovd.rickmorty.model.entities.location.Location
import com.gerasimovd.rickmorty.model.entities.location.RemoteLocationKey
import com.gerasimovd.rickmorty.model.remote.api.ApiService
import com.gerasimovd.rickmorty.utils.Constants
import com.gerasimovd.rickmorty.utils.Converter
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


@ExperimentalPagingApi
class LocationMediator @Inject constructor(
    private val apiService: ApiService,
    private val database: AppDatabase,
    private val isSearchMode: Boolean = false
) : RemoteMediator<Int, Location>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Location>
    ): MediatorResult {

        val pageKeyData = getKeyPageData(loadType, state)
        val page = when(pageKeyData) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData as Int
            }
        }

        if (isSearchMode == false) {
            try {
                val responseLocations = apiService.getLocations(page)
                val isEndOfList = responseLocations.body()?.locations?.isEmpty() ?: true
                database.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        database.getLocationDao().deleteAllLocations()
                        database.getRemoteLocationKeysDao().clearRemoteKeys()
                    }

                    val prevKey = if (page == Constants.START_PAGE) null else page -1
                    val nextKey = if (isEndOfList) null else page +1
                    val keys = responseLocations.body()?.locations?.map {
                        RemoteLocationKey(id = it.id, prevKey = prevKey, nextKey = nextKey)
                    }
                    keys?.let { database.getRemoteLocationKeysDao().insertAll(keys) }
                    responseLocations.body()?.let { locationsPage->
                        val locationsEntity = Converter.fromLocationsDtoToEntity(locationsPage.locations)
                        database.getLocationDao().insertLocations(locationsEntity)
                    }
                }
                return MediatorResult.Success(endOfPaginationReached = isEndOfList)
            }
            catch (e: IOException) { return MediatorResult.Error(e) }
            catch (e: HttpException) { return MediatorResult.Error(e) }
        }
        return MediatorResult.Success(endOfPaginationReached = true)
    }

    private suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, Location>): Any {
        return when(loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getClosestRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: Constants.START_PAGE
            }

            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                val nextKey = remoteKeys?.nextKey
                return nextKey ?: MediatorResult.Success(endOfPaginationReached = false)
            }

            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = false)
                prevKey
            }
        }
    }

    private suspend fun getClosestRemoteKey(state: PagingState<Int, Location>): RemoteLocationKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.getRemoteLocationKeysDao().remoteKeyLocationsPageId(id)
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, Location>): RemoteLocationKey? {
        return state.pages.lastOrNull {it.data.isNotEmpty()}
            ?.data?.lastOrNull()
            ?.id?.let { id -> database.getRemoteLocationKeysDao().remoteKeyLocationsPageId(id) }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, Location>): RemoteLocationKey? {
        return state.pages.firstOrNull {it.data.isNotEmpty()}
            ?.data?.firstOrNull()
            ?.id?.let { id -> database.getRemoteLocationKeysDao().remoteKeyLocationsPageId(id) }
    }
}