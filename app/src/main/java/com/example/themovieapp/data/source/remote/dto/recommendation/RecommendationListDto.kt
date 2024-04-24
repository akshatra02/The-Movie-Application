package com.example.themovieapp.data.source.remote.dto.recommendation

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class RecommendationListDto(
    val page: Int,
    val results: List<RecommendationDto>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)