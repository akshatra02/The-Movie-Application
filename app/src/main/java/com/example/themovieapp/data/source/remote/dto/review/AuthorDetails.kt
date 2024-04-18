package com.example.themovieapp.data.source.remote.dto.review

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable

data class AuthorDetails(
    @SerializedName("avatar_path") val avatarPath: String,
    val name: String,
    val rating: Double,
    val username: String
)