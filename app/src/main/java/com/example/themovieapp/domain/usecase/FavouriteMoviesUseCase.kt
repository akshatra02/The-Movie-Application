package com.example.themovieapp.domain.usecase

import com.example.themovieapp.data.source.remote.dto.favorites.FavouriteBody
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.domain.repository.MovieRepository
import com.example.themovieapp.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavouriteMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
)  {
    fun getFavouriteMoviesStream(): Flow<Resource<List<Movie>>> {
        return movieRepository.getFavouriteMoviesStream()
    }

    fun addMovieToFavouriteStream(movie: FavouriteBody): Flow<Resource<Movie>> {
        return movieRepository.addMovieToFavouriteStream(movie)
    }
}