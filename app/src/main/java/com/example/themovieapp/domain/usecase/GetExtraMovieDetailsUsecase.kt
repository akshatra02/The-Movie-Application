package com.example.themovieapp.domain.usecase


import com.example.themovieapp.domain.model.MovieDetailsAndExtraDetails
import com.example.themovieapp.domain.model.Review
import com.example.themovieapp.domain.repository.MovieDetailsRepository
import com.example.themovieapp.utils.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExtraMovieDetailsUsecase @Inject constructor(
    private val movieDetailsRepository: MovieDetailsRepository
)  {
    suspend operator fun invoke(id: Int): Result<Flow<MovieDetailsAndExtraDetails?>> {
        return movieDetailsRepository.getMovieAndExtraDetails(id)
    }

//    suspend fun addExtraMovieDetails(id: Int): Result<Flow<MovieDetailsAndExtraDetails>> {
//        return movieRepository.getExtraMovieDetails(id)
//    }

//    suspend fun getCastAndCrewStream(id: Int): Result<Flow<List<CastAndCrew>>> {
//        return movieDetailsRepository.getCastAndCrewStream(id)
//    }
    suspend fun getMovieReviewStream(id: Int): Result<Flow<List<Review>>> {
        return movieDetailsRepository.getMovieReviewStream(id)
    }




}