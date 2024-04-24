package com.example.themovieapp.domain.usecase

import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    operator fun invoke(): Flow<List<Movie>> {
        return movieRepository.getAllMoviesStream()
    }
}