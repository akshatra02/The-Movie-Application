package com.example.themovieapp.domain.model

data class Review(
    val movieId: Int,
    val id: String,
    val author: String,
    val content: String,
    val url: String,
    val rating: Double,
    val avatarPath: String?,
    val createdAt: String,
)
