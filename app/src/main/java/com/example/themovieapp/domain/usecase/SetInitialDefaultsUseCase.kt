package com.example.themovieapp.domain.usecase

import com.example.themovieapp.domain.repository.MovieRepository
import javax.inject.Inject

class SetInitialDefaultsUseCase  @Inject constructor(
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke(category: List<String>){
        return movieRepository.setInitialDefaults(category)
    }
}