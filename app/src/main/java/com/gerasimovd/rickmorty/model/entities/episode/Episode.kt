package com.gerasimovd.rickmorty.model.entities.episode

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


@Entity
data class Episode(
    @PrimaryKey val id: Int,
    val name: String,
    val air_date: String,
    val episode: String,
    val characters: List<String>,
    val url: String,
    val created: String
) {

    class CharactersConverter {
        @TypeConverter
        fun fromCharacters(characters: List<String>): String =
            Gson().toJson(characters)

        @TypeConverter
        fun toCharacters(jsonCharacters: String): List<String> =
            Gson().fromJson(jsonCharacters, object : TypeToken<List<String>>() {}.type)
    }
}