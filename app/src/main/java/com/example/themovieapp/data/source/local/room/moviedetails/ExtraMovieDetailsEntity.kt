package com.example.themovieapp.data.source.local.room.moviedetails

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.themovieapp.data.source.remote.dto.extramoviedetails.Genre
import com.example.themovieapp.domain.model.ExtraMovieDetails
import kotlinx.serialization.SerialName

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
    @ColumnInfo(name = "movie_id")val movieId: Int,
    val budget: Int,
    val genres: String,
    val homepage: String,
    @ColumnInfo("imdb_id") val imdbId: String,
    @ColumnInfo("original_language") val originalLanguage: String,
    @ColumnInfo("original_title") val originalTitle: String,
    val revenue: Int,
    val runtime: Int,
    val status: String,
    val tagline: String,
) {
    fun toExtraMovieDetails(): ExtraMovieDetails = ExtraMovieDetails(
        id = movieId,
        budget = budget,
        genres = genres.split(","),
        homepage = homepage,
        imdbId = imdbId,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        revenue = revenue,
        runtime = runtime,
        status = status,
        tagline = tagline,

        )
}
