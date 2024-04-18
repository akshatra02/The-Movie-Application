package com.example.themovieapp.domain.usecase

import com.example.themovieapp.domain.model.Review
import com.example.themovieapp.domain.repository.MovieDetailsRepository
import com.example.themovieapp.utils.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieReviewUseCase @Inject constructor(
    private val movieDetailsRepository: MovieDetailsRepository
)  {
    suspend operator fun invoke(id: Int): Result<Flow<List<Review>>> {
        return movieDetailsRepository.getMovieReviewStream(id)
    }
}