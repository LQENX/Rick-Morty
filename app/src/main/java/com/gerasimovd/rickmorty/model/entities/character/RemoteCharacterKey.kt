package com.gerasimovd.rickmorty.model.entities.character

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class RemoteCharacterKey(
    @PrimaryKey val id: Int,
    val prevKey: Int?,
    val nextKey: Int?
)