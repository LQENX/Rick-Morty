package com.gerasimovd.rickmorty.model.remote.dto.location

import com.google.gson.annotations.SerializedName


data class LocationsResponse(
    @SerializedName("results") val locations: List<LocationDto>
)