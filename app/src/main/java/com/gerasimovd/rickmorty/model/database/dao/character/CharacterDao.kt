package com.gerasimovd.rickmorty.model.database.dao.character

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gerasimovd.rickmorty.model.entities.character.Character
import kotlinx.coroutines.flow.Flow


@Dao
interface CharacterDao {

    @Query("SELECT * FROM Character")
    fun getAllCharacters(): PagingSource<Int, Character>

    @Query("SELECT * FROM Character WHERE name LIKE '%' || :characterName  || '%'")
    fun getCharactersByName(characterName: String): PagingSource<Int, Character>

    @Query("SELECT * FROM Character WHERE id = :id")
    fun getCharacterById(id: Int): Flow<Character>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<Character>)

    @Query("DELETE FROM Character")
    suspend fun deleteAllCharacters()
}