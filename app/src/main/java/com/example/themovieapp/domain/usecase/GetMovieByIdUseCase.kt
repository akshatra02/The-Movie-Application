package com.example.themovieapp.domain.usecase

import com.example.themovieapp.data.source.remote.dto.movielist.MovieListDto
import com.example.themovieapp.domain.model.ExtraMovieDetails
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.domain.repository.MovieRepository
import com.example.themovieapp.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieByIdUseCase @Inject constructor(
    private val movieRepository: MovieRepository
)  {
    operator fun invoke(id: Int): Flow<Resource<Movie>> {
        return movieRepository.getMovieByIdStream(id)
    }

   suspend fun addExtraMovieDetails(id: Int): Flow<Resource<ExtraMovieDetails>> {
        return movieRepository.addExtraMovieDetails(id)
    }
}