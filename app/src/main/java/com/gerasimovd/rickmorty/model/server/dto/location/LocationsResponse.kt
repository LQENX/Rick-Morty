package com.gerasimovd.rickmorty.model.server.dto.location

import com.google.gson.annotations.SerializedName


data class LocationsResponse(
    @SerializedName("results") val locations: List<LocationDto>
)