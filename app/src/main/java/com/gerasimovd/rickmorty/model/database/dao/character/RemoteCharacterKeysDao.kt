package com.gerasimovd.rickmorty.model.database.dao.character

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gerasimovd.rickmorty.model.entities.character.RemoteCharacterKey


@Dao
interface RemoteCharacterKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteCharacterKeys: List<RemoteCharacterKey>)

    @Query("SELECT * FROM RemoteCharacterKey WHERE id = :id")
    suspend fun remoteKeyCharactersPageId(id: Int): RemoteCharacterKey?

    @Query("DELETE FROM RemoteCharacterKey")
    suspend fun clearRemoteKeys()
}