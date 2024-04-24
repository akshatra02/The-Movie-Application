package com.example.themovieapp.data.source.remote.dto.extramoviedetails

import com.example.themovieapp.data.source.local.room.moviedetails.entity.ExtraMovieDetailsEntity
import com.example.themovieapp.data.source.local.room.moviedetails.entity.MovieEntity
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ExtraMovieDetailsDto(
    val id: Int?,
    val budget: Long?,
    val genres: List<Genre>?,
    val homepage: String?,
    @SerializedName(value = "imdb_id")  val imdbId: String?,
    @SerializedName(value = "original_language") val originalLanguage: String?,
    @SerializedName(value = "original_title")  val originalTitle: String?,
    val revenue: Long?,
    val runtime: Int?,
    val status: String?,
    val tagline: String?,
    val recommendationMoviesList : List<Int>?,
    val adult: Boolean?,
    @SerializedName (value = "backdrop_path") val backdropPath: String?,
    @SerializedName (value = "genre_ids") val genreIds: List<Genre>?,
    @SerializedName (value = "genre_names") val genreNames: List<String>?,
    val overview: String?,
    val popularity: Double?,
    @SerializedName (value = "poster_path") val posterPath: String?,
    @SerializedName (value = "release_date") val releaseDate: String?,
    val title: String?,
    val video: Boolean?,
    @SerializedName (value = "vote_average") val voteAverage: Double?,
    @SerializedName (value = "vote_count") val voteCount: Int?,
    val category: String,
    val isFavourite: Boolean,
    val videoLink: String?,
    val postersPathList: String?,
    val backdropsPathList: String?,
)