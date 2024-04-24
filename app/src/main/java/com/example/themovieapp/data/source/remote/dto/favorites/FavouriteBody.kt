package com.example.themovieapp.data.source.remote.dto.favorites

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class FavouriteBody(
    val favorite: Boolean,
    @SerializedName("media_id") val mediaId: Int,
    @SerializedName("media_type") val mediaType: String
)