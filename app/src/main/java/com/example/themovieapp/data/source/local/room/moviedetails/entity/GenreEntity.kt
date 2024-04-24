package com.example.themovieapp.data.source.local.room.moviedetails.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "genre_table")
data class GenreEntity(
    @PrimaryKey
    @ColumnInfo("genre_id") val genreId: Int,
    @ColumnInfo("genre_name") val genreName: String,
)
