package com.example.themovieapp.data.source.remote.dto.movieList

import com.example.themovieapp.data.source.local.room.MovieEntity

data class MovieDto(

    val adult: Boolean?,
    val backdrop_path: String?,
    val genre_ids: List<Int>?,
    val original_language: String?,
    val original_title: String?,
    val overview: String?,
    val popularity: Double?,
    val poster_path: String?,
    val release_date: String?,
    val title: String?,
    val video: Boolean?,
    val vote_average: Double?,
    val vote_count: Int?,
    val id: Int?,
    val category: String,

){
    fun toMovieEntity(category: String): MovieEntity{
        return MovieEntity(
            adult = adult ?: false,
            backdropPath = backdrop_path ?: "",
            originalLanguage = original_language ?: "",
            originalTitle = original_title ?: "",
            overview = overview ?: "",
            popularity = popularity ?: 0.0,
            posterPath = poster_path ?: "",
            releaseDate = release_date ?: "",
            title = title ?: "",
            video = video ?: false,
            voteAverage = vote_average ?: 0.0,
            voteCount = vote_count ?: 0,
            id = id ?: 0 ,
            category = category,
            genreIds = try {
                genre_ids?.joinToString { "," } ?: "-1,-2"
            }catch (e : Exception){
                "-1,-2"
            }

        )

    }
}