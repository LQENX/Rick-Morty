package com.gerasimovd.rickmorty.utils

import androidx.lifecycle.MutableLiveData


object NetworkManager {

    val IS_NETWORK_CONNECTED = MutableLiveData(false)

    fun setNetworkStatus(isConnected: Boolean) {
        IS_NETWORK_CONNECTED.value = isConnected
    }
}