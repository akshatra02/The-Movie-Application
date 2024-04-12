package com.example.themovieapp.domain.model

data class CastAndCrew(
    val movieId:Int,
    val personId:Int,
    val isCast: Boolean,
    val name: String,
    val profilePath: String,
    val role: String, //cast - character crew - job
    val knowForDepartment: String,
    val order: Int,
    val originalName: String,
)
