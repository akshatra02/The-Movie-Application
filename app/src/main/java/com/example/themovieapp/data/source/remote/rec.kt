package com.example.themovieapp.data.source.remote

import com.example.themovieapp.data.source.remote.dto.movielist.MovieDto


data class rec(
    val page: Int,
    val results: List<MovieDto>,
)