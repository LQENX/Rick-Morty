package com.gerasimovd.rickmorty.model.database.dao.location

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gerasimovd.rickmorty.model.entities.location.RemoteLocationKey


@Dao
interface RemoteLocationKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteLocationKeys: List<RemoteLocationKey>)

    @Query("SELECT * FROM RemoteLocationKey WHERE id = :id")
    suspend fun remoteKeyLocationsPageId(id: Int): RemoteLocationKey?

    @Query("DELETE FROM RemoteLocationKey")
    suspend fun clearRemoteKeys()
}