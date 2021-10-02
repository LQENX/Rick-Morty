package com.gerasimovd.rickmorty.model.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gerasimovd.rickmorty.model.entities.Character


@Dao
interface CharacterDao {

    @Query("SELECT * FROM Character")
    fun getAllCharacters(): PagingSource<Int, Character>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<Character>)

    @Query("DELETE FROM Character")
    suspend fun deleteAllCharacters()
}