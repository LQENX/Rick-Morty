package com.gerasimovd.rickmorty.model.database.dao.episode

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gerasimovd.rickmorty.model.entities.episode.Episode
import kotlinx.coroutines.flow.Flow

@Dao
interface EpisodeDao {

    @Query("SELECT * FROM Episode")
    fun getAllEpisodes(): PagingSource<Int, Episode>

    @Query("SELECT * FROM Episode WHERE id IN (:episodesId)")
    fun getEpisodesById(episodesId: List<Int>): PagingSource<Int, Episode>

    @Query("SELECT * FROM Episode WHERE id LIKE '%' || :episodeId  || '%'")
    fun getEpisodeById(episodeId: Int): Flow<Episode>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisodes(episodes: List<Episode>)

    @Query("DELETE FROM Episode")
    suspend fun deleteAllEpisodes()
}