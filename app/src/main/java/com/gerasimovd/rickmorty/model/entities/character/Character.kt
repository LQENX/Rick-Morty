package com.gerasimovd.rickmorty.model.entities.character

import androidx.room.*
import com.gerasimovd.rickmorty.model.remote.dto.character.CharacterLocationDto
import com.gerasimovd.rickmorty.model.remote.dto.character.CharacterOriginDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


@Entity
data class Character(
    @PrimaryKey val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    @Embedded(prefix = "origin") val origin: CharacterOriginDto,
    @Embedded(prefix = "location") val location: CharacterLocationDto,
    val image: String,
    val episodesId: List<Int>, // Difference from CharacterDto
    val url: String,
    val created: String
) {

    class EpisodesIdConverter {
        @TypeConverter
        fun fromEpisodesId(episodesId: List<Int>): String =
            Gson().toJson(episodesId)

        @TypeConverter
        fun toEpisodesId(jsonEpisodesId: String): List<Int> =
            Gson().fromJson(jsonEpisodesId, object : TypeToken<List<Int>>() {}.type)
    }
}