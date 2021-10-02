package com.gerasimovd.rickmorty.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gerasimovd.rickmorty.model.database.dao.CharacterDao
import com.gerasimovd.rickmorty.model.database.dao.RemoteKeysDao
import com.gerasimovd.rickmorty.model.entities.Character
import com.gerasimovd.rickmorty.model.entities.RemoteKey


@Database(entities = [Character::class, RemoteKey::class], version = 1, exportSchema = false)
@TypeConverters(Character.EpisodesIdConverter::class)
abstract class AppDatabase : RoomDatabase(){

    abstract fun getCharacterDao(): CharacterDao
    abstract fun getRemoteKeysDao(): RemoteKeysDao
}