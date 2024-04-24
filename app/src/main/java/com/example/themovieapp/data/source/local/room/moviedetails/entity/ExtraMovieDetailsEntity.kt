package com.example.themovieapp.data.source.local.room.moviedetails.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "extra_movie_details_table",
    foreignKeys = [ForeignKey(
        entity = MovieEntity::class,
        parentColumns = ["id"],
        childColumns = ["movie_id"],
    )]
)
data class ExtraMovieDetailsEntity(
    @PrimaryKey
    @ColumnInfo(name = "movie_id") val movieId: Int,

    val budget: Long,
    val homepage: String,
    val revenue: Long,
    val runtime: Int,
    val status: String,
    val tagline: String,
    @ColumnInfo("imdb_id") val imdbId: String,
    @ColumnInfo("recommendation_movies_list") val recommendationMoviesList: String,
    @ColumnInfo("posters_path_list") val postersPathList: String,
    @ColumnInfo("backdrops_path_list") val backdropsPathList: String,
    @ColumnInfo("video_link") val videoLink: String,

)