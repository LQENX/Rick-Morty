package com.gerasimovd.rickmorty.model.server.entities

import com.gerasimovd.rickmorty.model.server.dto.character.CharacterLocationDto
import com.gerasimovd.rickmorty.model.server.dto.character.CharacterOriginDto
import com.google.gson.annotations.SerializedName


data class CharacterEntity(
    var id: Int,
    var name: String,
    var status: String,
    var species: String,
    var type: String,
    var gender: String,
    var origin: CharacterOriginDto,
    var location: CharacterLocationDto,
    var image: String,
    var episodes: List<EpisodeEntity>,
    var url: String,
    var created: String
)