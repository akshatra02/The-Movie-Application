package com.example.themovieapp.data.source.remote.dto.movielist

import com.example.themovieapp.domain.model.GenreList

data class GenreDto(
    val id: Int,
    val name: String
){
    fun toGenreList(): GenreList =
        GenreList(
            id = id,
            name = name
        )
}