package com.example.themovieapp.data.source.remote.dto.review

data class ReviewListDto(
    val id: Int,
    val page: Int,
    val results: List<ReviewDto>,
    val total_pages: Int,
    val total_results: Int
)