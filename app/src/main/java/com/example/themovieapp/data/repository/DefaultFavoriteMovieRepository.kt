package com.example.themovieapp.data.repository

import com.example.themovieapp.data.source.local.room.moviedetails.MovieEntityDao
import com.example.themovieapp.data.source.remote.MoviesApi
import com.example.themovieapp.data.source.remote.dto.favorites.FavouriteBody
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.domain.repository.FavoriteMovieRepository
import com.example.themovieapp.domain.repository.MovieRepository
import com.example.themovieapp.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class DefaultFavoriteMovieRepository  @Inject constructor(
    val movieEntityDao: MovieEntityDao,
    val moviesApiService: MoviesApi,
) : FavoriteMovieRepository {
    override suspend fun getFavouriteMoviesStream(): Result<Flow<List<Movie>>> {
        return try {
            Result.Success(movieEntityDao.getFavouriteMovies().map { movieEntityList ->
                movieEntityList.map { movieEntity ->
                    movieEntity.toMovie(movieEntity.category)
                }
            })


        } catch (e: HttpException) {
            Result.Error("Oops Something went wrong! Try again later.")


        } catch (e: IOException) {

            Result.Error("Couldn't reach server. Check your internet connection")


        }
    }

    override suspend fun addMovieToFavourite(movie: FavouriteBody): Boolean {
        val response = moviesApiService.addMovieToFavourite(RAW_BODY = movie)
        val getMovie = movieEntityDao.getMovieById(movie.media_id).first()
        if (response.isSuccessful) {
            val updateMovie = getMovie?.copy(
                isFavourite = !getMovie.isFavourite
            )
            if (updateMovie != null) {
                movieEntityDao.updateMovie(updateMovie)
                return true
            }
        }
        return false
    }
}