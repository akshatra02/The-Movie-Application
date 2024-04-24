package com.example.themovieapp.data.source.remote.dto.recommendation

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class RecommendationDto(
    val adult: Boolean?,
    val category: String?,
    val id: Int?,
    val overview: String?,
    val popularity: Double?,
    val title: String?,
    val video: Boolean?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("genre_ids") val genreIds: List<Int>?,
    @SerializedName("media_type") val mediaType: String?,
    @SerializedName("original_title")  val originalTitle: String?,
    @SerializedName("original_language") val originalLanguage: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("vote_average") val voteAverage: Double?,
    @SerializedName("vote_count")  val voteCount: Int?,
)