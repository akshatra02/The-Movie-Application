package com.example.themovieapp.domain.model


data class ExtraMovieDetails(
    val id: Int,
    val budget: Int,
    val genres: List<String>,
    val homepage: String,
    val imdbId: String,
    val originalLanguage: String,
    val originalTitle: String,
    val revenue: Int,
    val runtime: Int,
    val status: String,
    val tagline: String,
)