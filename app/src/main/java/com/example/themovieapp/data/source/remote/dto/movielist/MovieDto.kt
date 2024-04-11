package com.example.themovieapp.data.source.remote.dto.movielist

import com.example.themovieapp.data.source.local.room.moviedetails.MovieEntity
import kotlinx.serialization.SerialName

data class MovieDto(

    val adult: Boolean?,
    @SerialName("backdrop_path") val backdropPath: String?,
    @SerialName("genre_ids") val genreIds: List<Int>?,
    @SerialName("genre_names")val genreNames: List<String>?,
    @SerialName("original_language")val originalLanguage: String?,
    @SerialName("original_title") val originalTitle: String?,
    val overview: String?,
    val popularity: Double?,
    @SerialName("poster_path")  val posterPath: String?,
    @SerialName("release_date") val releaseDate: String?,
    val title: String?,
    val video: Boolean?,
    @SerialName("vote_average") val voteAverage: Double?,
    @SerialName("vote_count") val voteCount: Int?,
    val id: Int?,
    val category: String,
    val isFavourite: Boolean,

    ){
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
            id = id ?: 0 ,
            category = category,
            genreIds = try {
                genreIds?.joinToString { "," } ?: "-1,-2"
            }catch (e : Exception){
                "-1,-2"
            },
            genreNames = genreNames,
            isFavourite = isFavourite


        )

    }
}