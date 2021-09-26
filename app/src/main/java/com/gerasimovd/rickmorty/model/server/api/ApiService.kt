package com.gerasimovd.rickmorty.model.server.api

import com.gerasimovd.rickmorty.model.server.dto.character.CharactersResponse
import com.gerasimovd.rickmorty.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET(Constants.CHARACTER_END_POINT)
    suspend fun getCharacters(@Query("page") page: Int): Response<CharactersResponse>
}