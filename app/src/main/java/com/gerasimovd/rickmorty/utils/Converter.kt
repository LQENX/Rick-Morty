package com.gerasimovd.rickmorty.utils

import com.gerasimovd.rickmorty.model.entities.character.Character
import com.gerasimovd.rickmorty.model.entities.episode.Episode
import com.gerasimovd.rickmorty.model.remote.dto.character.CharacterDto
import com.gerasimovd.rickmorty.model.remote.dto.episode.EpisodeDto


object Converter {

    fun fromCharactersDtoToEntity(charactersDto: List<CharacterDto>): List<Character> {
        val charactersEntity = mutableListOf<Character>()

        for (dto in charactersDto) {
            val charactersEpisodesId = mutableListOf<Int>()

            for (dtoEpisode in dto.episodes) {
                charactersEpisodesId.add(dtoEpisode.filter { it.isDigit() }.toInt())
            }
            charactersEntity.add(
                Character(
                    id = dto.id,
                    name = dto.name,
                    status = dto.status,
                    species = dto.species,
                    type = dto.type,
                    gender = dto.gender,
                    origin = dto.origin,
                    location = dto.location,
                    image = dto.image,
                    episodesId = charactersEpisodesId,
                    url = dto.url,
                    created = dto.created)
            )
        }

        return charactersEntity
    }

    fun fromEpisodesDtoToEntity(episodesDto: List<EpisodeDto>): List<Episode> {
        val episodesEntity = mutableListOf<Episode>()

        for (dto in episodesDto) {
            episodesEntity.add(
                Episode(
                    id = dto.id,
                    name = dto.name,
                    air_date = dto.air_date,
                    episode = dto.episode,
                    characters = dto.characters,
                    url = dto.url,
                    created = dto.created
                )
            )
        }
        return episodesEntity
    }
}