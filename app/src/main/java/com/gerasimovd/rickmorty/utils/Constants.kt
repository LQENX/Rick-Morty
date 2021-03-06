package com.gerasimovd.rickmorty.utils

class Constants {
    companion object {
        const val BASE_URL = "https://rickandmortyapi.com/api/"
        const val CHARACTER_END_POINT = "character"
        const val EPISODE_END_POINT = "episode"
        const val LOCATION_END_POINT = "location"

        const val READ_TIMEOUT = 30L

        const val START_PAGE = 1

        const val NETWORK_INTENT_ACTION = "android.net.conn.CONNECTIVITY_CHANGE"
    }
}