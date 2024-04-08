package com.example.themovieapp.data.repository

import com.example.themovieapp.data.source.local.room.MovieEntity
import com.example.themovieapp.data.source.local.room.MovieEntityDao
import com.example.themovieapp.data.source.remote.MoviesApi
import com.example.themovieapp.data.source.remote.Resource
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    val movieEntityDao: MovieEntityDao,
    val moviesApiService: MoviesApi,
    ): MovieRepository {


    override fun getMovies(category: String, page: Int): Flow<Resource<List<Movie>>> = flow{
        emit(Resource.Loading())
        try {
            fetchAndInsertMovieList(movieEntityDao,moviesApiService,category,page)
        }
        catch (e : HttpException){
            emit(Resource.Error("Oops Something went wrong! Try again later."))
        }
        catch (e : IOException){
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        }

        emit(Resource.Success(getMovieListFromDb(movieEntityDao,category)))
    }

    override fun getMovieById(id: Int): Flow<Resource<Movie>> = flow {
        emit(Resource.Loading())
        try {
            val getMovieById = movieEntityDao.getMovieById(id)
            emit(Resource.Success(getMovieById.toMovie(getMovieById.category)))
        }
        catch (e : HttpException){
            emit(Resource.Error("Oops Something went wrong! Try again later."))
        }
        catch (e : IOException){
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        }


    }

    override fun getAllMovies(): Flow<List<Movie>> = flow {
       emit(movieEntityDao.getAllMovies().map { it.toMovie(it.category) })
    }
}
private suspend fun fetchAndInsertMovieList(
    movieEntityDao: MovieEntityDao,
    moviesApiService: MoviesApi,
    category: String,
    page: Int,
){
    val remoteMovieList = moviesApiService.getMovieList(category,page)
    movieEntityDao.insertMovieList(remoteMovieList.results.map { it.toMovieEntity(category) })


}
private suspend fun getMovieListFromDb(
    movieEntityDao: MovieEntityDao,
    category: String
): List<Movie>{
    return movieEntityDao.getMovieList(category).map { it.toMovie(category) }
}