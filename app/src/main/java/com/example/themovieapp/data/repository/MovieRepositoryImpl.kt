package com.example.themovieapp.data.repository

import com.example.themovieapp.data.source.local.room.MovieEntityDao
import com.example.themovieapp.data.source.remote.MoviesApi
import com.example.themovieapp.data.source.remote.dto.favorites.FavouriteBody
import com.example.themovieapp.data.source.remote.dto.movieList.GenreDto
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.domain.repository.MovieRepository
import com.example.themovieapp.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    val movieEntityDao: MovieEntityDao,
    val moviesApiService: MoviesApi,
) : MovieRepository {

    override fun getMoviesByCategory(
        category: String, forceFetchFromRemote: Boolean, page: Int
    ): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading(true))
            val localMovieList = movieEntityDao.getMovieListByCategory(category)
            val shouldLoadLoadMovies = localMovieList.isNotEmpty() && !forceFetchFromRemote

            if (shouldLoadLoadMovies) {
                emit(Resource.Success(getMovieListFromDb(movieEntityDao, category)))
                return@flow
            }
        try {
            fetchAndInsertMovieList(
                movieEntityDao, moviesApiService, category, page
            )
        } catch (e: HttpException) {
            emit(Resource.Error("Oops Something went wrong! Try again later."))
            return@flow
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
            return@flow

        }
        emit(Resource.Success(getMovieListFromDb(movieEntityDao, category)))
        return@flow

    }

    override fun getMovieById(id: Int): Flow<Resource<Movie>> = flow {
        emit(Resource.Loading())
        try {
            val getMovieById = movieEntityDao.getMovieById(id)
            emit(Resource.Success(getMovieById.toMovie(getMovieById.category)))
        } catch (e: HttpException) {
            emit(Resource.Error("Oops Something went wrong! Try again later."))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        }


    }

    override fun getAllMovies(): Flow<List<Movie>> = flow {
        emit(movieEntityDao.getAllMovies().map { it.toMovie(it.category) })
    }

    override fun getFavouriteMovies(): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading())
        try {
            emit(
                Resource.Success(movieEntityDao.getFavouriteMovies()
                    .map { it.toMovie(it.category) })
            )
        } catch (e: HttpException) {
            emit(Resource.Error("Oops Something went wrong! Try again later."))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        }
    }

    override fun addMovieToFavourite(movie: FavouriteBody): Flow<Resource<Movie>> = flow {
        emit(Resource.Loading())
        try {
            emit(
                Resource.Success(
                    addMovieToFavouriteRemote(
                        moviesApiService, movieEntityDao, movie
                    )
                )
            )
        } catch (e: HttpException) {
            emit(Resource.Error("Oops Something went wrong! Try again later."))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        }
    }
}

private suspend fun addMovieToFavouriteRemote(
    moviesApiService: MoviesApi, movieEntityDao: MovieEntityDao, movie: FavouriteBody
): Movie {
    val response = moviesApiService.addMovieToFavourite(RAW_BODY = movie)
    val getMovie = movieEntityDao.getMovieById(movie.media_id)
    val movieEntityStatus = movieEntityDao.updateMovie(getMovie)
    return getMovie.toMovie(getMovie.category)

}

private suspend fun fetchAndInsertMovieList(
    movieEntityDao: MovieEntityDao,
    moviesApiService: MoviesApi,
    category: String,
    page: Int,
) {
    val remoteMovieList = moviesApiService.getMovieList(category, page)
    val genreList = getGenreList(moviesApiService)
    val favouriteMovieList = moviesApiService.getFavouriteMovies().results
    val movieEntities = remoteMovieList.results.map { movieDto ->
        val genreNames =
            genreList.filter { it.id in (movieDto.genre_ids ?: emptyList()) }.map { it.name }
        var isFavourite = favouriteMovieList.any { it.id == movieDto.id }
        movieDto.toMovieEntity(category, genreNames.joinToString(","), isFavourite)
    }
    movieEntityDao.insertMovieList(movieEntities)
}

suspend fun getGenreList(
    moviesApiService: MoviesApi,

    ): List<GenreDto> {
    return moviesApiService.getGenreList().genres
}


private suspend fun getMovieListFromDb(
    movieEntityDao: MovieEntityDao, category: String
): List<Movie> {
    return movieEntityDao.getMovieListByCategory(category).map { it.toMovie(category) }
}