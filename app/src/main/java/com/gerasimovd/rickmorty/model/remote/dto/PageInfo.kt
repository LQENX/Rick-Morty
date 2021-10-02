package com.gerasimovd.rickmorty.model.remote.dto

import com.google.gson.annotations.SerializedName


data class PageInfo(
    @SerializedName("prev") val prevPage: String?,
    @SerializedName("next") val nextPage: String?
)