package com.gerasimovd.rickmorty.model.remote.dto.character

import com.google.gson.annotations.SerializedName


data class CharactersPageResponse(
    @SerializedName("results") val characters: List<CharacterDto>
)