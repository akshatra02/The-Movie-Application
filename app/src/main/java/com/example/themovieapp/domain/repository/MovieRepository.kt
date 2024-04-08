package com.example.themovieapp.domain.repository


import com.example.themovieapp.data.source.local.room.MovieEntity
import com.example.themovieapp.data.source.remote.Resource
import com.example.themovieapp.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getMovies(category: String, page: Int):Flow<Resource<List<Movie>>>
     fun getMovieById(id : Int): Flow<Resource<Movie>>

     fun getAllMovies():Flow<List<Movie>>


}