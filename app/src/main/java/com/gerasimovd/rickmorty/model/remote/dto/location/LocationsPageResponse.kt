package com.gerasimovd.rickmorty.model.remote.dto.location

import com.google.gson.annotations.SerializedName


data class LocationsPageResponse(
    @SerializedName("results") val episodes: List<LocationDto>
)