package com.gerasimovd.rickmorty.utils

import com.gerasimovd.rickmorty.model.entities.Character
import com.gerasimovd.rickmorty.model.remote.dto.character.CharacterDto


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
}