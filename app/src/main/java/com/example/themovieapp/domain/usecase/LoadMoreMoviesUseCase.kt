package com.example.themovieapp.domain.usecase

import com.example.themovieapp.domain.repository.MovieRepository
import com.example.themovieapp.utils.Result
import javax.inject.Inject

class LoadMoreMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(
        category: String,
    ): Result<String> {
        return movieRepository.loadMoreMovies(category)
    }
}