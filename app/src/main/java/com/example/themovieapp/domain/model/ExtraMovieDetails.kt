package com.example.themovieapp.domain.model

data class ExtraMovieDetails(
    val id: Int,
    val budget: Long?,
    val homepage: String,
    val imdbId: String,
    val revenue: Long?,
    val runtime: Int,
    val status: String,
    val tagline: String?,
    val recommendationMoviesList: List<Int>?,
    val imagesPathList: List<String>,
    val videoLink: String?,
)