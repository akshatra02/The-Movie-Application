package com.example.themovieapp.domain.repository


import com.example.themovieapp.data.source.remote.dto.favorites.FavouriteBody
import com.example.themovieapp.data.source.remote.dto.movielist.MovieListDto
import com.example.themovieapp.domain.model.ExtraMovieDetails
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.utils.Resource
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getMoviesByCategoryStream(category: String,forceFetchFromRemote: Boolean, page: Int): Flow<Resource<List<Movie>>>
    fun getMovieByIdStream(id: Int): Flow<Resource<Movie>>

    fun getAllMoviesStream(): Flow<List<Movie>>

     fun getFavouriteMoviesStream():Flow<Resource<List<Movie>>>

     fun addMovieToFavouriteStream(movie: FavouriteBody): Flow<Resource<Movie>>

     suspend fun addExtraMovieDetails(id: Int): Flow<Resource<ExtraMovieDetails>>

}