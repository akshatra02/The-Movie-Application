package com.example.themovieapp.domain.usecase

import com.example.themovieapp.data.repository.MovieRepositoryImpl
import com.example.themovieapp.data.source.remote.Resource
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieList @Inject constructor(
    private val movieRepository: MovieRepositoryImpl
) {
    operator fun invoke(category: String): Flow<Resource<List<Movie>>>{
        return movieRepository.getMovies(category)
    }
}