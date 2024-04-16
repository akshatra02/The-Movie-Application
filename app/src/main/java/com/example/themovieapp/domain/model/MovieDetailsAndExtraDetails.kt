package com.example.themovieapp.domain.model

import androidx.room.ColumnInfo
data class MovieDetailsAndExtraDetails(
    val id: Int,
    val category: String,
    val isFavourite: Boolean,
    val adult: Boolean,
    val backdropPath: String,
    val genreIds: List<Int>,
    val genreNames: List<String>,
    val originalLanguage: String?,
    val originalTitle: String?,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val releaseDate: String,
    val title: String,
    val video: Boolean,
    val voteAverage: Double?,
    val voteCount: Int?,

    val movieId: Int?,
    val budget: Long?,
    val homepage: String?,
    val revenue: Long?,
    val runtime: Int?,
    val status: String?,
    val tagline: String?,
    val imdbId: String?,
    val recommendationMoviesList: List<Int>?,
    val imagesPathList: List<String>?,
    val videoLink: String?,
)