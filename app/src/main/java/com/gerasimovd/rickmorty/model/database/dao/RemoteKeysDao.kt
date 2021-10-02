package com.gerasimovd.rickmorty.model.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gerasimovd.rickmorty.model.entities.RemoteKey


@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<RemoteKey>)

    @Query("SELECT * FROM RemoteKey WHERE id = :id")
    suspend fun remoteKeyCharactersPageId(id: Int): RemoteKey?

    @Query("DELETE FROM RemoteKey")
    suspend fun clearRemoteKeys()
}