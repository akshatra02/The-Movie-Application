package com.example.themovieapp.data.source.remote.dto.movielist

import com.example.themovieapp.data.source.local.room.moviedetails.MovieEntity
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDto(

    val adult: Boolean?,
    @SerializedName (value = "backdrop_path") val backdropPath: String?,
    @SerializedName (value = "genre_ids") val genreIds: List<Int>?,
    @SerializedName (value = "genre_names") val genreNames: List<String>?,
    @SerializedName (value = "original_language") val originalLanguage: String?,
    @SerializedName (value = "original_title") val originalTitle: String?,
    val overview: String?,
    val popularity: Double?,
    @SerializedName (value = "poster_path") val posterPath: String?,
    @SerializedName (value = "release_date") val releaseDate: String?,
    val title: String?,
     val video: Boolean?,
    @SerializedName (value = "vote_average") val voteAverage: Double?,
    @SerializedName (value = "vote_count") val voteCount: Int?,
    val id: Int?,
    val category: String,
    val isFavourite: Boolean,

    ) {
    fun toMovieEntity(category: String, genreNames: String, isFavourite: Boolean): MovieEntity {
        return MovieEntity(
            adult = adult ?: false,
            backdropPath = backdropPath ?: "",
            originalLanguage = originalLanguage ?: "",
            originalTitle = originalTitle ?: "",
            overview = overview ?: "",
            popularity = popularity ?: 0.0,
            posterPath = posterPath ?: "",
            releaseDate = releaseDate ?: "",
            title = title ?: "",
            video = video ?: false,
            voteAverage = voteAverage ?: 0.0,
            voteCount = voteCount ?: 0,
            id = id ?: 0,
            category = category,
            genreIds = try {
                genreIds.toString()
            } catch (e: Exception) {
                "-1,-2"
            },
            genreNames = genreNames,
            isFavourite = isFavourite,
        )
    }
}