package com.example.themovieapp.data.source.remote.dto.castandcrew

import com.example.themovieapp.data.source.local.room.moviedetails.entity.CastAndCrewEntity
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class CrewDto(
    val adult: Boolean?,
    val department: String?,
    val gender: Int?,
    val id: Int,
    val job: String?,
    val name: String,
    val popularity: Double?,
    @SerializedName("credit_id") val creditId: String?,
    @SerializedName("known_for_department")  val knownForDepartment: String?,
    @SerializedName("original_name")  val originalName: String?,
    @SerializedName("profile_path")  val profilePath: String?
)