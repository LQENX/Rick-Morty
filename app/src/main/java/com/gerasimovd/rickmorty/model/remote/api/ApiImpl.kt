package com.gerasimovd.rickmorty.model.remote.api

import com.gerasimovd.rickmorty.di.AppModule
import com.gerasimovd.rickmorty.model.remote.dto.character.CharacterDto
import com.gerasimovd.rickmorty.model.remote.dto.episode.EpisodeDto


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