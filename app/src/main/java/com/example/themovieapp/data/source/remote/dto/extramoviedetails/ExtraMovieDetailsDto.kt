package com.example.themovieapp.data.source.remote.dto.extramoviedetails

import com.example.themovieapp.data.source.local.room.moviedetails.ExtraMovieDetailsEntity
import com.google.gson.Gson
import com.google.gson.JsonObject
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
) {
    fun toExtraMovieDetailsEntity() = ExtraMovieDetailsEntity(
        movieId = id ?: 0,
        budget = budget ?: 0,
        genres = try {
//            Gson().fromJson(genres,JsonObject::class.java)
            genres?.forEach { it.name }.toString()
        } catch (e: Exception) {
                               ""
        },
        homepage = homepage ?: "",
        imdbId = imdbId ?: "",
        originalLanguage = originalLanguage ?: "",
        originalTitle = originalTitle ?: "",
        revenue = revenue ?: 0,
        runtime = runtime ?: 0,
        status = status ?: "",
        tagline = tagline ?: "",

        )
}