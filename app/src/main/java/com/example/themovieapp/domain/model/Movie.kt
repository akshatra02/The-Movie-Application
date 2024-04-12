package com.example.themovieapp.domain.model

data class Movie (
        val adult: Boolean,
        val backdropPath: String,
        val genreIds: List<Int>,
        val genreNames: List<String>,
        val originalLanguage: String,
        val originalTitle: String,
        val overview: String,
        val popularity: Double,
        val posterPath: String,
        val releaseDate: String,
        val title: String,
        val video: Boolean,
        val voteAverage: Double,
        val voteCount: Int,
        val id: Int,
        val category: String,
        val isFavourite: Boolean,
)