package com.example.themovieapp.data.source.remote.dto.castandcrew

import com.example.themovieapp.data.source.local.room.moviedetails.entity.CastAndCrewEntity
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class CastDto(
    val adult: Boolean,
    @SerializedName("cast_id") val castId: Int?,
    val character: String?,
    val gender: Int?,
    val id: Int,
    val name: String,
    val order: Int?,
    val popularity: Double?,
    @SerializedName("credit_id") val creditId: String?,
    @SerializedName("known_for_department") val knownForDepartment: String?,
    @SerializedName("original_name") val originalName: String?,
    @SerializedName("profile_path") val profilePath: String?
) {

    fun toCastAndCrewEntity(movieId : Int): CastAndCrewEntity =
        CastAndCrewEntity(
            personId = id,
            movieId = movieId,
            name = name,
            role = character ?: "",
            knowForDepartment = knownForDepartment ?: "",
            order = order ?: -1,
            profilePath = profilePath ?: "",
            isCast = true,
        )
}