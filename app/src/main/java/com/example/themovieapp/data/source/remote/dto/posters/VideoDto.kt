package com.example.themovieapp.data.source.remote.dto.posters

data class VideoDto(
    val id: Int,
    val results: List<Video>
)

data class Video(
    val key: String,
    val type: String
)