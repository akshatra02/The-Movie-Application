package com.example.themovieapp.domain.repository

import com.example.themovieapp.data.source.remote.dto.favorites.FavouriteBody
import com.example.themovieapp.data.source.remote.dto.movielist.MovieListDto
import com.example.themovieapp.domain.model.CastAndCrew
import com.example.themovieapp.domain.model.ExtraMovieDetails
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.domain.model.MovieDetailsAndExtraDetails
import com.example.themovieapp.domain.model.Review
import com.example.themovieapp.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface MovieRepository {
    fun getMoviesByCategoryStream(category: String,forceFetchFromRemote: Boolean, page: Int): Flow<Resource<List<Movie>>>

    fun getMovieByIdStream(id: Int): Flow<Resource<Movie>>
    fun getAllMoviesStream(): Flow<List<Movie>>

     fun getFavouriteMoviesStream():Flow<Resource<List<Movie>>>

     fun addMovieToFavouriteStream(movie: FavouriteBody): Flow<Resource<MovieDetailsAndExtraDetails>>

     fun addExtraMovieDetails(id: Int): Flow<Resource<MovieDetailsAndExtraDetails>>

     fun getCastAndCrewStream(id: Int): Flow<Resource<List<CastAndCrew>>>

     fun getMovieReviewStream(id: Int): Flow<Resource<List<Review>>>

     fun getMovieAndExtraDetails(id:Int): Flow<Resource<MovieDetailsAndExtraDetails?>>

}