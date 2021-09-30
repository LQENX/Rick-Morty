package com.gerasimovd.rickmorty.viewmodel

import androidx.lifecycle.ViewModel
import com.gerasimovd.rickmorty.model.server.dto.episode.EpisodeDto


class CharacterInfoViewModel : ViewModel() {

    private var characterEpisodes = listOf<EpisodeDto>()

    fun setCharacterEpisodes(episodes: List<EpisodeDto>) {characterEpisodes = episodes}
    fun getCharacterEpisodes() = characterEpisodes

}