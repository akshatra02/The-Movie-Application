package com.example.themovieapp.data.source.remote.dto.review

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewListDto(
    val id: Int,
    val page: Int,
    val results: List<ReviewDto>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)