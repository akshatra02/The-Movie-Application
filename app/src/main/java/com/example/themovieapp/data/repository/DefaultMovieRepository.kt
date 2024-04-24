package com.example.themovieapp.data.repository

import com.example.themovieapp.data.mappers.toDomainMovie
import com.example.themovieapp.data.mappers.toEntityGenre
import com.example.themovieapp.data.mappers.toEntityMovie
import com.example.themovieapp.data.source.local.room.moviedetails.MovieEntityDao
import com.example.themovieapp.data.source.local.room.moviedetails.entity.SyncEntity
import com.example.themovieapp.data.source.remote.MoviesApi
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
    private val movieEntityDao: MovieEntityDao,
    private val moviesApiService: MoviesApi,
) : MovieRepository {
    override suspend fun getMoviesByCategoryStream(category: String): Result<Flow<List<Movie>>> {
        val localMovieList = movieEntityDao.getMovieListByCategory(category).first()
        if (localMovieList.isEmpty()) {
            loadMoreMovies(category)
        }
        return try {
            Result.Success(getMovieListFromDb(category))
        } catch (e: HttpException) {
            Result.Error("Oops Something went wrong! Try again later.")
        } catch (e: IOException) {
            Result.Error("Couldn't reach server. Check your internet connection")
        }
    }

    override fun getAllMoviesStream(): Flow<List<Movie>> {
        return movieEntityDao.getAllMovies().map { movieEntityList ->
            movieEntityList.map { movieEntity ->
                movieEntity.toDomainMovie(movieEntity.category)
            }
        }
    }

    override suspend fun setInitialDefaults(categories: List<String>) {
        val syncDetails = movieEntityDao.getSyncDetails()
        if (syncDetails == null) {
            for (category in categories) {
                movieEntityDao.upsertSyncDetails(SyncEntity(category, 1))
            }
            val genreRemote = moviesApiService.getGenreList().genres
            movieEntityDao.insertGenreDetails(genreRemote.map { it.toEntityGenre() })
            val genreList = movieEntityDao.getGenreDetails()
            val favouriteMovieList = moviesApiService.getFavouriteMovies().results
            val favouriteMovieEntities = favouriteMovieList.map { favMovieDto ->
                val genreNames =
                    genreList.filter { it.genreId in (favMovieDto.genreIds ?: emptyList()) }
                        .map { it.genreName }
                favMovieDto.toEntityMovie("", genreNames.joinToString(","), isFavourite = true)
            }
            movieEntityDao.insertMovieList(favouriteMovieEntities)
        }
    }

    override suspend fun loadMoreMovies(category: String): Result<String> {
        return try {
            val syncDetails = movieEntityDao.getSyncDetails()
            val page = syncDetails?.page ?: 0
            val remoteMovieList = moviesApiService.getMovieList(category, page)
            val genreList = movieEntityDao.getGenreDetails()
            val movieEntities = remoteMovieList.results.map { movieDto ->
                val genreNames =
                    genreList.filter { it.genreId in (movieDto.genreIds ?: emptyList()) }
                        .map { it.genreName }
                movieDto.toEntityMovie(category, genreNames.joinToString(","), isFavourite = false)
            }
            movieEntityDao.insertMovieList(movieEntities)
            movieEntityDao.upsertSyncDetails(SyncEntity(category, page + 1))
            Result.Success("Done")
        } catch (e: HttpException) {
            Result.Error("Oops Something went wrong! Try again later.")
        } catch (e: IOException) {
            Result.Error("Couldn't reach server. Check your internet connection")
        }
    }

    private fun getMovieListFromDb(
        category: String
    ): Flow<List<Movie>> {
        return movieEntityDao.getMovieListByCategory(category).map { movieEntityList ->
            movieEntityList.map { movieEntity ->
                movieEntity.toDomainMovie(movieEntity.category)
            }
        }
    }
}