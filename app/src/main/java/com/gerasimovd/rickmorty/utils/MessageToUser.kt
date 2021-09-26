package com.gerasimovd.rickmorty.utils

import android.content.Context
import android.widget.Toast


class MessageToUser {

    companion object {
        fun sendToast(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
            Toast.makeText(context, message, duration).show()
        }
    }
}