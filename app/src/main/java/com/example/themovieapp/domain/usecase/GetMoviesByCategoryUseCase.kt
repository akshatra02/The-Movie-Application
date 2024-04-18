package com.example.themovieapp.domain.usecase

import com.example.themovieapp.data.source.local.room.moviedetails.entity.MovieEntity
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.domain.repository.MovieRepository
import com.example.themovieapp.utils.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesByCategoryUseCase  @Inject constructor(
    private val movieRepository: MovieRepository
){
    suspend operator fun invoke(
        category: String,
        forceFetchFromRemote: Boolean,
        page: Int
    ): Result<Flow<List<Movie>>> {
        return movieRepository.getMoviesByCategoryStream(category, forceFetchFromRemote, page)
    }
}