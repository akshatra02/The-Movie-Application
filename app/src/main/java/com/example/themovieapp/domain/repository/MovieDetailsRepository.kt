package com.example.themovieapp.domain.repository

import com.example.themovieapp.data.source.remote.dto.favorites.FavouriteBody
import com.example.themovieapp.domain.model.CastAndCrew
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.domain.model.MovieDetailsAndExtraDetails
import com.example.themovieapp.domain.model.Review
import com.example.themovieapp.utils.Result
import kotlinx.coroutines.flow.Flow

interface MovieDetailsRepository {
    suspend fun addExtraMovieDetails(id: Int)

    suspend fun getCastAndCrewStream(id: Int): Result<Flow<List<CastAndCrew>>>

    suspend fun getMovieReviewStream(id: Int): Result<Flow<List<Review>>>

    suspend fun getMovieAndExtraDetails(id: Int): Result<Flow<MovieDetailsAndExtraDetails?>>

    suspend fun getFavouriteMoviesStream(): Result<Flow<List<Movie>>>

    suspend fun updateFavouriteStatus(movie: FavouriteBody): Boolean
}