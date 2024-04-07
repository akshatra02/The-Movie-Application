package com.example.themovieapp.data.source.remote.dto.movieList

import com.example.themovieapp.data.source.remote.dto.movieList.MovieDto

data class MovieListDto(
    val page: Int,
    val results: List<MovieDto>,
    val total_pages: Int,
    val total_results: Int
)