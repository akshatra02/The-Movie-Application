package com.example.themovieapp.domain.repository

import com.example.themovieapp.data.source.remote.dto.favorites.FavouriteBody
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.utils.Result
import kotlinx.coroutines.flow.Flow


interface FavoriteMovieRepository {
    suspend fun getFavouriteMoviesStream(): Result<Flow<List<Movie>>>

    suspend fun addMovieToFavourite(movie: FavouriteBody): Boolean
}