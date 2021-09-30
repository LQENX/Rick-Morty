package com.gerasimovd.rickmorty.model.server.entities


data class EpisodeEntity(
    var id: Int,
    var name: String,
    var air_date: String,
    var episode: String,
    var characters: List<String>,
    var url: String,
    var created: String
)
