package com.example.themovieapp.domain.usecase

import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.domain.repository.FavoriteMovieRepository
import com.example.themovieapp.domain.repository.MovieRepository
import com.example.themovieapp.utils.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavouriteMoviesUseCase @Inject constructor(
    private val movieRepository: FavoriteMovieRepository
)  {
    suspend operator fun invoke(): Result<Flow<List<Movie>>> {
        return movieRepository.getFavouriteMoviesStream()
    }
}