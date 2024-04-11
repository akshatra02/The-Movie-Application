package com.example.themovieapp.data.repository

import com.example.themovieapp.data.source.local.room.moviedetails.MovieEntityDao
import com.example.themovieapp.data.source.remote.MoviesApi
import com.example.themovieapp.data.source.remote.dto.favorites.FavouriteBody
import com.example.themovieapp.data.source.remote.dto.movielist.GenreDto
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.domain.repository.MovieRepository
import com.example.themovieapp.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class DefaultMovieRepository @Inject constructor(
    val movieEntityDao: MovieEntityDao,
    val moviesApiService: MoviesApi,
) : MovieRepository {

    override fun getMoviesByCategoryStream(
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

    override fun getMovieByIdStream(id: Int): Flow<Resource<Movie>> = flow {
        emit(Resource.Loading())
        try {
            val getMovieById = movieEntityDao.getMovieById(id)
            emit(Resource.Success(getMovieById.toMovie(getMovieById.category)))
            return@flow
        } catch (e: HttpException) {
            emit(Resource.Error("Oops Something went wrong! Try again later."))
            return@flow

        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
            return@flow

        }


    }

    override fun getAllMoviesStream(): Flow<List<Movie>> = flow {
        emit(movieEntityDao.getAllMovies().map { it.toMovie(it.category) })
        return@flow

    }

    override fun getFavouriteMoviesStream(): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading(true))
        try {
            emit(
                Resource.Success(movieEntityDao.getFavouriteMovies()
                    .map { it.toMovie(it.category) })
            )
            return@flow

        } catch (e: HttpException) {
            emit(Resource.Error("Oops Something went wrong! Try again later."))
            return@flow

        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
            return@flow

        }
    }

    override fun addMovieToFavouriteStream(movie: FavouriteBody): Flow<Resource<Movie>> = flow {
        emit(Resource.Loading())
        try {
            emit(
                Resource.Success(
                    addMovieToFavouriteRemote(
                        moviesApiService, movieEntityDao, movie
                    )
                )
            )
            return@flow

        } catch (e: HttpException) {
            emit(Resource.Error("Oops Something went wrong! Try again later."))
            return@flow

        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
            return@flow

        }
    }
}

private suspend fun addMovieToFavouriteRemote(
    moviesApiService: MoviesApi, movieEntityDao: MovieEntityDao, movie: FavouriteBody
): Movie {
    val response = moviesApiService.addMovieToFavourite(RAW_BODY = movie)
    val getMovie = movieEntityDao.getMovieById(movie.media_id)
    if (response.isSuccessful) {
        val updateMovie = getMovie.copy(isFavourite = !getMovie.isFavourite)
        val movieEntityStatus = movieEntityDao.updateMovie(updateMovie)
    }
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
            genreList.filter { it.id in (movieDto.genreIds ?: emptyList()) }.map { it.name }
        val isFavourite = favouriteMovieList.any { it.id == movieDto.id }
        movieDto.toMovieEntity(category, genreNames.joinToString(","), isFavourite)
    }
    movieEntityDao.insertMovieList(movieEntities)
}

private suspend fun getGenreList(
    moviesApiService: MoviesApi,

    ): List<GenreDto> {
    return moviesApiService.getGenreList().genres
}


private suspend fun getMovieListFromDb(
    movieEntityDao: MovieEntityDao, category: String
): List<Movie> {
    return movieEntityDao.getMovieListByCategory(category).map { it.toMovie(category) }
}