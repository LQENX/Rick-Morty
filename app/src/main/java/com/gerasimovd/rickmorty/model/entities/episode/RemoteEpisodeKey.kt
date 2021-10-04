package com.gerasimovd.rickmorty.model.entities.episode

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class RemoteEpisodeKey(
    @PrimaryKey val id: Int,
    val prevKey: Int?,
    val nextKey: Int?
)