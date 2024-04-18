package com.example.themovieapp.domain.usecase

import com.example.themovieapp.data.source.remote.dto.favorites.FavouriteBody
import com.example.themovieapp.domain.repository.FavoriteMovieRepository
import javax.inject.Inject

class AddFavouriteMovieUseCase @Inject constructor(
    private val movieRepository: FavoriteMovieRepository
)  {
    suspend operator fun invoke(movie: FavouriteBody): Boolean{
        return movieRepository.addMovieToFavourite(movie)
    }
}