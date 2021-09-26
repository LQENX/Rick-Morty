package com.gerasimovd.rickmorty.model.server.dto.character

import com.google.gson.annotations.SerializedName


data class CharactersResponse(
    @SerializedName("results") val characters: List<CharacterDto>
)