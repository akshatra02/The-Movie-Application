package com.example.themovieapp.domain.repository

import com.example.themovieapp.domain.model.CastAndCrew
import com.example.themovieapp.domain.model.MovieDetailsAndExtraDetails
import com.example.themovieapp.domain.model.Review
import com.example.themovieapp.utils.Result
import kotlinx.coroutines.flow.Flow

interface MovieDetailsRepository {
    suspend fun getExtraMovieDetails(id: Int): Result<Flow<MovieDetailsAndExtraDetails>>

    suspend fun getCastAndCrewStream(id: Int): Result<Flow<List<CastAndCrew>>>

    suspend fun getMovieReviewStream(id: Int): Result<Flow<List<Review>>>

    suspend fun getMovieAndExtraDetails(id: Int): Result<Flow<MovieDetailsAndExtraDetails?>>
}