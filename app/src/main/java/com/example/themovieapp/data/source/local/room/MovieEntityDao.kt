package com.example.themovieapp.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieEntityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieList(movie: List<MovieEntity>)

    @Query("SELECT * FROM movie_table WHERE category = :category")
    suspend fun getMovieList(category: String): List<MovieEntity>

    @Query("SELECT * FROM movie_table WHERE id = :id")
    suspend fun getMovieById(id : Int): MovieEntity

    @Query("SELECT * FROM movie_table")
    suspend fun getAllMovies():List<MovieEntity>
}