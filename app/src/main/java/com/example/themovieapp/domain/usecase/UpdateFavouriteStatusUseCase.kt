package com.example.themovieapp.domain.usecase

import com.example.themovieapp.data.source.remote.dto.favorites.FavouriteBody
import com.example.themovieapp.domain.repository.MovieDetailsRepository
import javax.inject.Inject

class UpdateFavouriteStatusUseCase @Inject constructor(
    private val movieRepository: MovieDetailsRepository
) {
    suspend operator fun invoke(movie: FavouriteBody): Boolean {
        return movieRepository.updateFavouriteStatus(movie)
    }
}