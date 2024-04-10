package com.example.themovieapp.domain.repository


import com.example.themovieapp.data.source.local.room.MovieEntity
import com.example.themovieapp.data.source.remote.dto.favorites.FavouriteBody
import com.example.themovieapp.data.source.remote.dto.movieList.GenreDto
import com.example.themovieapp.data.source.remote.dto.movieList.GenreListDto
import com.example.themovieapp.data.source.remote.dto.movieList.MovieDto
import com.example.themovieapp.data.source.remote.dto.movieList.MovieListDto
import com.example.themovieapp.domain.model.GenreList
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.utils.Resource
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response

interface MovieRepository {
    fun getMoviesByCategory(category: String,forceFetchFromRemote: Boolean, page: Int): Flow<Resource<List<Movie>>>
    fun getMovieById(id: Int): Flow<Resource<Movie>>

    fun getAllMovies(): Flow<List<Movie>>

     fun getFavouriteMovies():Flow<Resource<List<Movie>>>

     fun addMovieToFavourite(movie: FavouriteBody): Flow<Resource<Movie>>

}