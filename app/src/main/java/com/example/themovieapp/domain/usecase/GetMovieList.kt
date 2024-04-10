package com.example.themovieapp.domain.usecase

import com.example.themovieapp.data.source.remote.dto.favorites.FavouriteBody
import com.example.themovieapp.data.source.remote.dto.movieList.MovieDto
import com.example.themovieapp.data.source.remote.dto.movieList.MovieListDto
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.domain.repository.MovieRepository
import com.example.themovieapp.utils.Resource
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class GetMovieList @Inject constructor(
    private val movieRepository: MovieRepository
) {
    fun getMoviesByCategory(
        category: String,
        forceFetchFromRemote: Boolean,
        page: Int
    ): Flow<Resource<List<Movie>>> {
        return movieRepository.getMoviesByCategory(category, forceFetchFromRemote, page)
    }

    fun getMovieById(id: Int): Flow<Resource<Movie>> {
        return movieRepository.getMovieById(id)
    }

    fun getAllMovies(): Flow<List<Movie>> {
        return movieRepository.getAllMovies()
    }

    fun getFavouriteMovies(): Flow<Resource<List<Movie>>> {
        return movieRepository.getFavouriteMovies()
    }

    fun addMovieToFavourite(movie: FavouriteBody): Flow<Resource<Movie>> {
        return movieRepository.addMovieToFavourite(movie)
    }

}