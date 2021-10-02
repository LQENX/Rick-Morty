package com.gerasimovd.rickmorty.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class RemoteKey(
    @PrimaryKey val id: Int,
    val prevKey: Int?,
    val nextKey: Int?
)