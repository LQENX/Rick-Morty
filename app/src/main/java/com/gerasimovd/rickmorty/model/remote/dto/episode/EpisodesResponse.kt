package com.gerasimovd.rickmorty.model.remote.dto.episode

import com.google.gson.annotations.SerializedName


data class EpisodesResponse(
    @SerializedName("results") val episodes: List<EpisodeDto>
)
