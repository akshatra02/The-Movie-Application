package com.example.themovieapp.data.repository

import com.example.themovieapp.data.source.local.room.moviedetails.MovieEntityDao
import com.example.themovieapp.data.source.remote.MoviesApi
import com.example.themovieapp.data.source.remote.dto.movielist.GenreDto
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.domain.repository.MovieRepository
import com.example.themovieapp.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class DefaultMovieRepository @Inject constructor(
    val movieEntityDao: MovieEntityDao,
    val moviesApiService: MoviesApi,
) : MovieRepository {


    override suspend fun getMoviesByCategoryStream(
        category: String, forceFetchFromRemote: Boolean, page: Int
    ): Result<Flow<List<Movie>>> {
        val localMovieList = movieEntityDao.getMovieListByCategory(category).first()
        if (localMovieList.isNotEmpty() && !forceFetchFromRemote) {
            return Result.Success(getMovieListFromDb(movieEntityDao, category))
        }
        return try {
            fetchAndInsertMovieList(
                movieEntityDao, moviesApiService, category, page
            )
            Result.Success(getMovieListFromDb(movieEntityDao, category))

        } catch (e: HttpException) {
            Result.Error("Oops Something went wrong! Try again later.")
        } catch (e: IOException) {
            Result.Error("Couldn't reach server. Check your internet connection")

        }

    }

    override fun getAllMoviesStream(): Flow<List<Movie>> {
        return movieEntityDao.getAllMovies().map { movieEntityList ->
            movieEntityList.map { movieEntity ->
                movieEntity.toMovie(movieEntity.category)
            }
        }
    }
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

    val favouriteMovieEntities = favouriteMovieList.map { favMovieDto ->
        val genreNames =
            genreList.filter { it.id in (favMovieDto.genreIds ?: emptyList()) }.map { it.name }
        favMovieDto.toMovieEntity("", genreNames.joinToString(","), isFavourite = true)
    }
    movieEntityDao.insertMovieList(favouriteMovieEntities)
}

private suspend fun getGenreList(
    moviesApiService: MoviesApi,

    ): List<GenreDto> {
    return moviesApiService.getGenreList().genres
}


private fun getMovieListFromDb(
    movieEntityDao: MovieEntityDao, category: String
): Flow<List<Movie>> {
    return movieEntityDao.getMovieListByCategory(category).map { movieEntityList ->
        movieEntityList.map { movieEntity ->
            movieEntity.toMovie(movieEntity.category)
        }
    }
}
