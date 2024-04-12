package com.example.themovieapp.data.source.remote.dto.extramoviedetails

import com.example.themovieapp.data.source.local.room.moviedetails.entity.ExtraMovieDetailsEntity
import com.example.themovieapp.data.source.local.room.moviedetails.entity.MovieEntity
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ExtraMovieDetailsDto(
    val id: Int?,
    val budget: Int?,
    val genres: List<Genre>?,
    val homepage: String?,
    @SerializedName(value = "imdb_id")  val imdbId: String?,
    @SerializedName(value = "original_language") val originalLanguage: String?,
    @SerializedName(value = "original_title")  val originalTitle: String?,
    val revenue: Int?,
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
) {
    fun toExtraMovieDetailsEntity(recommendationMoviesList: List<Int>) = ExtraMovieDetailsEntity(
        movieId = id ?: 0,
        budget = budget ?: 0,
        homepage = homepage ?: "",
        imdbId = imdbId ?: "",
        originalLanguage = originalLanguage ?: "",
        originalTitle = originalTitle ?: "",
        revenue = revenue ?: 0,
        runtime = runtime ?: 0,
        status = status ?: "",
        tagline = tagline ?: "",
        recommendationMoviesList = recommendationMoviesList.toString()

    )

    fun toMovieEntity(
        category: String,
        genreNames: String,
        genreIds: String,
        isFavourite: Boolean,
    ): MovieEntity {
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
            genreIds = genreIds,
            genreNames = genreNames,
            isFavourite = isFavourite,
        )
    }
}