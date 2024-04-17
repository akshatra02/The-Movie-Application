package com.example.themovieapp.data.source.remote.dto.recommendation

import com.example.themovieapp.data.source.remote.dto.recommendation.RecommendationDto

data class RecommendationListDto(
    val page: Int,
    val results: List<RecommendationDto>,
    val total_pages: Int,
    val total_results: Int
)