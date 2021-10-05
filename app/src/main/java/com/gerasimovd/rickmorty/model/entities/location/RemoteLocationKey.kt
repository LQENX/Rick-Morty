package com.gerasimovd.rickmorty.model.entities.location

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class RemoteLocationKey(
    @PrimaryKey val id: Int,
    val prevKey: Int?,
    val nextKey: Int?
)