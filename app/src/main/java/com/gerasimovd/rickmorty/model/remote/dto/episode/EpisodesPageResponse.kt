package com.gerasimovd.rickmorty.model.remote.dto.episode

import com.google.gson.annotations.SerializedName


data class EpisodesPageResponse(
    @SerializedName("results") val episodes: List<EpisodeDto>
)
