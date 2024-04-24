package com.example.themovieapp.domain.usecase

import com.example.themovieapp.domain.model.CastAndCrew
import com.example.themovieapp.domain.repository.MovieDetailsRepository
import com.example.themovieapp.utils.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCastAndCrewUseCase @Inject constructor(
    private val movieDetailsRepository: MovieDetailsRepository
) {
    suspend operator fun invoke(id: Int): Result<Flow<List<CastAndCrew>>> {
        return movieDetailsRepository.getCastAndCrewStream(id)
    }
}