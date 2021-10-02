package com.gerasimovd.rickmorty.model.remote.api

import com.gerasimovd.rickmorty.model.remote.dto.character.CharacterDto
import com.gerasimovd.rickmorty.model.remote.dto.character.CharactersPageResponse
import com.gerasimovd.rickmorty.model.remote.dto.episode.EpisodeDto
import com.gerasimovd.rickmorty.model.remote.dto.episode.EpisodesResponse
import com.gerasimovd.rickmorty.model.remote.dto.location.LocationsResponse
import com.gerasimovd.rickmorty.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {

    @GET(Constants.CHARACTER_END_POINT)
    suspend fun getCharacters(@Query("page") page: Int): Response<CharactersPageResponse>

    @GET("${Constants.CHARACTER_END_POINT}/{id}")
    suspend fun getCharacterById(@Path("id") characterId: Int): Response<CharacterDto>

    @GET(Constants.LOCATION_END_POINT)
    suspend fun getLocations(@Query("page") page: Int): Response<LocationsResponse>

    @GET("${Constants.EPISODE_END_POINT}/{id}")
    suspend fun getSingleEpisode(@Path("id") episodeId: String): Response<EpisodeDto>

    @GET("${Constants.EPISODE_END_POINT}/{ids}")
    suspend fun getEpisodesById(@Path("ids") episodesId: String): Response<List<EpisodeDto>>

    @GET(Constants.EPISODE_END_POINT)
    suspend fun getAllEpisodes(): Response<EpisodesResponse>
}