package com.gerasimovd.rickmorty.model.database.dao.episode

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gerasimovd.rickmorty.model.entities.episode.RemoteEpisodeKey


@Dao
interface RemoteEpisodeKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteEpisodeKeys: List<RemoteEpisodeKey>)

    @Query("SELECT * FROM RemoteEpisodeKey WHERE id = :id")
    suspend fun remoteKeyEpisodesPageId(id: Int): RemoteEpisodeKey?

    @Query("DELETE FROM RemoteEpisodeKey")
    suspend fun clearRemoteKeys()
}