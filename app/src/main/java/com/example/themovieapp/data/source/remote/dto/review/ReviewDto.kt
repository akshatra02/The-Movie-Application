package com.example.themovieapp.data.source.remote.dto.review

import com.example.themovieapp.data.source.local.room.moviedetails.entity.ReviewEntity
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewDto(
    val author: String?,
    val content: String?,
    val id: String?,
    val url: String?,
    @SerializedName("author_details") val authorDetails: AuthorDetails?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?,
)