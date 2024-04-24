package com.example.themovieapp.data.source.remote.dto.movielist

import com.example.themovieapp.data.source.local.room.moviedetails.entity.MovieEntity
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDto(

    val adult: Boolean?,
    val overview: String?,
    val popularity: Double?,
    val title: String?,
    val video: Boolean?,
    @SerializedName (value = "backdrop_path") val backdropPath: String?,
    @SerializedName (value = "genre_ids") val genreIds: List<Int>?,
    @SerializedName (value = "genre_names") val genreNames: List<String>?,
    @SerializedName (value = "original_language") val originalLanguage: String?,
    @SerializedName (value = "original_title") val originalTitle: String?,
    @SerializedName (value = "poster_path") val posterPath: String?,
    @SerializedName (value = "release_date") val releaseDate: String?,
    @SerializedName (value = "vote_average") val voteAverage: Double?,
    @SerializedName (value = "vote_count") val voteCount: Int?,
    val id: Int?,
    val category: String,
    val isFavourite: Boolean,

    )