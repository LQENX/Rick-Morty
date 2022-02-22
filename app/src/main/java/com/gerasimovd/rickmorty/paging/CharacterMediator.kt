package com.gerasimovd.rickmorty.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.gerasimovd.rickmorty.model.database.AppDatabase
import com.gerasimovd.rickmorty.model.entities.character.Character
import com.gerasimovd.rickmorty.model.entities.character.RemoteCharacterKey
import com.gerasimovd.rickmorty.model.remote.api.ApiService
import com.gerasimovd.rickmorty.utils.Constants
import com.gerasimovd.rickmorty.utils.Converter
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


@ExperimentalPagingApi
class CharacterMediator @Inject constructor(
    private val apiService: ApiService,
    private val database: AppDatabase,
    private val isSearchMode: Boolean = false
) : RemoteMediator<Int, Character>() {

    do some changes in file to commit
    some else
        anY&>>

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Character>
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
                val responseCharacters = apiService.getCharacters(page)
                val isEndOfList = responseCharacters.body()?.characters?.isEmpty() ?: true
                database.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        database.getCharacterDao().deleteAllCharacters()
                        database.getRemoteCharacterKeysDao().clearRemoteKeys()
                    }

                    val prevKey = if (page == Constants.START_PAGE) null else page -1
                    val nextKey = if (isEndOfList) null else page +1
                    val keys = responseCharacters.body()?.characters?.map {
                        RemoteCharacterKey(id = it.id, prevKey = prevKey, nextKey = nextKey)
                    }
                    keys?.let { database.getRemoteCharacterKeysDao().insertAll(keys) }
                    responseCharacters.body()?.let {charactersPage->
                        val charactersEntity = Converter.fromCharactersDtoToEntity(charactersPage.characters)
                        database.getCharacterDao().insertCharacters(charactersEntity)
                    }
                }
                return MediatorResult.Success(endOfPaginationReached = isEndOfList)
            }
            catch (e: IOException) { return MediatorResult.Error(e) }
            catch (e: HttpException) { return MediatorResult.Error(e) }
        }
        return MediatorResult.Success(endOfPaginationReached = true)
    }

    private suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, Character>): Any {
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

    private suspend fun getClosestRemoteKey(state: PagingState<Int, Character>): RemoteCharacterKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.getRemoteCharacterKeysDao().remoteKeyCharactersPageId(id)
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, Character>): RemoteCharacterKey? {
        return state.pages.lastOrNull {it.data.isNotEmpty()}
            ?.data?.lastOrNull()
            ?.id?.let { id -> database.getRemoteCharacterKeysDao().remoteKeyCharactersPageId(id) }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, Character>): RemoteCharacterKey? {
        return state.pages.firstOrNull {it.data.isNotEmpty()}
            ?.data?.firstOrNull()
            ?.id?.let { id -> database.getRemoteCharacterKeysDao().remoteKeyCharactersPageId(id) }
    }
}