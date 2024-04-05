package com.example.themovieapp.domain.repository


import com.example.themovieapp.data.source.remote.Resource
import com.example.themovieapp.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getMovies(category: String):Flow<Resource<List<Movie>>>
}