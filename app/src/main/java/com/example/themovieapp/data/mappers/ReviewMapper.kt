package com.example.themovieapp.data.mappers

import com.example.themovieapp.data.source.local.room.moviedetails.entity.ReviewEntity
import com.example.themovieapp.data.source.remote.dto.review.ReviewDto
import com.example.themovieapp.domain.model.Review

fun ReviewEntity.toDomainReview(): Review = Review(
    movieId = movieId,
    id = id,
    author = author,
    content = content,
    url = url,
    rating = rating,
    avatarPath = avatarPath,
    createdAt = createdAt,
)
fun ReviewDto.toEntityReview(movieId: Int): ReviewEntity = ReviewEntity(
    movieId = movieId,
    id = id ?: "",
    author = author ?: "",
    content = content ?: "",
    url = url ?: "",
    rating = authorDetails?.rating?:0.0,
    avatarPath = authorDetails?.avatarPath ?: "",
    createdAt = createdAt ?: "",
)