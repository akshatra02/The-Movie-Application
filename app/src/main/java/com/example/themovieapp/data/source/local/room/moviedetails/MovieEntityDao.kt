package com.example.themovieapp.data.source.local.room.moviedetails

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface MovieEntityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieList(movie: List<MovieEntity>)

    @Query("SELECT * FROM movie_table WHERE category = :category")
    suspend fun getMovieListByCategory(category: String): List<MovieEntity>

    @Query("SELECT * FROM movie_table WHERE id = :id")
    suspend fun getMovieById(id : Int): MovieEntity

    @Query("SELECT * FROM movie_table")
    suspend fun getAllMovies():List<MovieEntity>

    @Query("SELECT * FROM movie_table WHERE is_favourite = 1")
    suspend fun getFavouriteMovies():List<MovieEntity>

    @Update
    suspend fun updateMovie(movieEntity: MovieEntity): Int


}