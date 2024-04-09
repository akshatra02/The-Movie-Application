package com.example.themovieapp.data.source.remote.dto.favorites

data class FavouriteBody(
    val favorite: Boolean,
    val media_id: Int,
    val media_type: String
)