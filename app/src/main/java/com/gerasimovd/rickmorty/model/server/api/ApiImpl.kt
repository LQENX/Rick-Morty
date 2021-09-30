package com.gerasimovd.rickmorty.model.server.api

import com.gerasimovd.rickmorty.di.AppModule
import com.gerasimovd.rickmorty.model.server.dto.character.CharacterDto
import com.gerasimovd.rickmorty.model.server.dto.episode.EpisodeDto
import com.gerasimovd.rickmorty.model.server.dto.episode.EpisodesResponse


object ApiImpl {

    suspend fun getCharacterById(characterId: Int): CharacterDto? {
        val response = AppModule.provideRetrofitInstance(AppModule.provideBaseUrl(), AppModule.provideReadTimeout()).getCharacterById(characterId)

        return if (response.isSuccessful) { response.body() }
        else null
    }

    suspend fun getEpisodesById(episodesId: String): List<EpisodeDto>? {
        val response = AppModule.provideRetrofitInstance(AppModule.provideBaseUrl(), AppModule.provideReadTimeout()).getEpisodesById(episodesId)

        return if (response.isSuccessful) { response.body() }
        else null
    }
}