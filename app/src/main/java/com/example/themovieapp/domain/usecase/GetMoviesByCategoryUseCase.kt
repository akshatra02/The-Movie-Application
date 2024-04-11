package com.example.themovieapp.domain.usecase

import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.domain.repository.MovieRepository
import com.example.themovieapp.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesByCategoryUseCase  @Inject constructor(
    private val movieRepository: MovieRepository
){
    operator fun invoke(
        category: String,
        forceFetchFromRemote: Boolean,
        page: Int
    ): Flow<Resource<List<Movie>>> {
        return movieRepository.getMoviesByCategoryStream(category, forceFetchFromRemote, page)
    }
}