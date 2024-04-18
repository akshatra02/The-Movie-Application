package com.example.themovieapp.domain.usecase

import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.domain.repository.MovieRepository
import com.example.themovieapp.utils.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieByIdUseCase @Inject constructor(
    private val movieRepository: MovieRepository
)  {
    suspend operator fun invoke(id: Int): Result<Flow<Movie?>> {
        return movieRepository.getMovieByIdStream(id)
    }
}