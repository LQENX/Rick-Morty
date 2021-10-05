package com.gerasimovd.rickmorty.model.database.dao.location

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gerasimovd.rickmorty.model.entities.location.Location
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Query("SELECT * FROM Location")
    fun getAllLocations(): PagingSource<Int, Location>

    @Query("SELECT * FROM Location WHERE name LIKE '%' || :locationName  || '%'")
    fun getLocationsByName(locationName: String): PagingSource<Int, Location>

    @Query("SELECT * FROM Location WHERE id =:locationId")
    fun getLocationById(locationId: Int): Flow<Location>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocations(episodes: List<Location>)

    @Query("DELETE FROM Location")
    suspend fun deleteAllLocations()
}