package com.example.themovieapp.domain.usecase

import com.example.themovieapp.data.repository.MovieRepositoryImpl
import com.example.themovieapp.data.source.remote.Resource
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieList @Inject constructor(
    private val movieRepository: MovieRepository
) {
     fun getMovieList(category: String, page: Int): Flow<Resource<List<Movie>>>{
        return movieRepository.getMovies(category,page)
    }
    fun getMovieById(id: Int): Flow<Resource<Movie>>{
        return movieRepository.getMovieById(id)
    }

    fun getAllMovies(): Flow<List<Movie>>{
        return movieRepository.getAllMovies()
    }
}