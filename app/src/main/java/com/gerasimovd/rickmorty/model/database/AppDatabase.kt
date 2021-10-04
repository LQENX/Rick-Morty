package com.gerasimovd.rickmorty.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gerasimovd.rickmorty.model.database.dao.character.CharacterDao
import com.gerasimovd.rickmorty.model.database.dao.character.RemoteCharacterKeysDao
import com.gerasimovd.rickmorty.model.database.dao.episode.EpisodeDao
import com.gerasimovd.rickmorty.model.database.dao.episode.RemoteEpisodeKeysDao
import com.gerasimovd.rickmorty.model.entities.character.Character
import com.gerasimovd.rickmorty.model.entities.episode.Episode
import com.gerasimovd.rickmorty.model.entities.character.RemoteCharacterKey
import com.gerasimovd.rickmorty.model.entities.episode.RemoteEpisodeKey


@Database(
    entities = [
        Character::class, RemoteCharacterKey::class,
        Episode::class, RemoteEpisodeKey::class],
    version = 1,
    exportSchema = false)
@TypeConverters(
    Character.EpisodesIdConverter::class,
    Episode.CharactersConverter::class
)
abstract class AppDatabase : RoomDatabase(){

    abstract fun getCharacterDao(): CharacterDao
    abstract fun getRemoteCharacterKeysDao(): RemoteCharacterKeysDao

    abstract fun getEpisodeDao(): EpisodeDao
    abstract fun getRemoteEpisodeKeysDao(): RemoteEpisodeKeysDao
}