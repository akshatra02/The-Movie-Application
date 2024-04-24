package com.example.themovieapp.data.source.local.room.moviedetails.entity

import androidx.room.ColumnInfo
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.domain.model.MovieDetailsAndExtraDetails

data class MovieDetailsAndExtraDetailsData(
    val id: Int,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "is_favourite")val isFavourite: Boolean,
    @ColumnInfo(name = "adult") val adult: Boolean,
    @ColumnInfo(name = "backdrop_path") val backdropPath: String,
    @ColumnInfo(name = "genre_ids")  val genreIds: String,
    @ColumnInfo(name = "genre_names")  val genreNames: String,
    @ColumnInfo(name = "original_language")  val originalLanguage: String?,
    @ColumnInfo(name = "original_title")  val originalTitle: String?,
    @ColumnInfo(name = "overview")  val overview: String,
    @ColumnInfo(name = "popularity")  val popularity: Double,
    @ColumnInfo(name = "poster_path")  val posterPath: String,
    @ColumnInfo(name = "release_date")  val releaseDate: String,
    @ColumnInfo(name = "title")  val title: String,
    @ColumnInfo(name = "video")  val video: Boolean,
    @ColumnInfo(name = "vote_average")  val voteAverage: Double?,
    @ColumnInfo(name = "vote_count")  val voteCount: Int?,

    @ColumnInfo(name = "movie_id") val movieId: Int?,
    val budget: Long?,
    val homepage: String?,
    val revenue: Long?,
    val runtime: Int?,
    val status: String?,
    val tagline: String?,
    @ColumnInfo("imdb_id") val imdbId: String?,
    @ColumnInfo("recommendation_movies_list") val recommendationMoviesList: String?,
    @ColumnInfo("posters_path_list") val postersPathList: String?,
    @ColumnInfo("backdrops_path_list") val backdropsPathList: String?,
    @ColumnInfo("video_link") val videoLink: String?,
)