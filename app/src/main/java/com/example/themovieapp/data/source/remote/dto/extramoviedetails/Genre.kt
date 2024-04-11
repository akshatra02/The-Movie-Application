package com.example.themovieapp.data.source.remote.dto.extramoviedetails

import kotlinx.serialization.Serializable

@Serializable
data class Genre(
    val id: Int,
    val name: String
)