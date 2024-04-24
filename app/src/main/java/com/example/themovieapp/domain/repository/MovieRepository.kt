package com.example.themovieapp.domain.repository

import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.utils.Result
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getMoviesByCategoryStream(category: String, ): Result<Flow<List<Movie>>>
    fun getAllMoviesStream(): Flow<List<Movie>>
    suspend fun setInitialDefaults(categories: List<String>)
    suspend fun loadMoreMovies(category: String, ):Result<String>
}