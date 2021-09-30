package com.gerasimovd.rickmorty.utils


interface ItemClickListener {
    fun <T> onClick(item: T)
}