package com.example.themovieapp.data.source.local.room.moviedetails

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.themovieapp.data.source.local.room.moviedetails.entity.CastAndCrewEntity
import com.example.themovieapp.data.source.local.room.moviedetails.entity.ExtraMovieDetailsEntity
import com.example.themovieapp.data.source.local.room.moviedetails.entity.MovieDetailsAndExtraDetailsData
import com.example.themovieapp.data.source.local.room.moviedetails.entity.MovieEntity
import com.example.themovieapp.data.source.local.room.moviedetails.entity.ReviewEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieEntityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieList(movie: List<MovieEntity>)

    @Upsert
    suspend fun upsertMovie(movie: MovieEntity)

    @Query("SELECT * FROM movie_table WHERE category = :category")
    fun getMovieListByCategory(category: String): Flow<List<MovieEntity>>

    @Transaction
    @Query("SELECT * FROM movie_table")
    fun getAllMovies(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movie_table WHERE is_favourite = 1")
    fun getFavouriteMovies(): Flow<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExtraMovieDetails(extraMovieDetailsEntity: ExtraMovieDetailsEntity)

    @Query(
        """SELECT movie.*, extradetails.* 
FROM movie_table AS movie 
LEFT JOIN EXTRA_MOVIE_DETAILS_TABLE AS extradetails 
ON movie.id = extradetails.movie_id
WHERE movie.id = :id
"""
    )
    fun getMovieDetailsById(id: Int): Flow<MovieDetailsAndExtraDetailsData>?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCastAndCrew(castAndCrewEntity: CastAndCrewEntity)

    @Query("SELECT * FROM cast_and_crew_table WHERE movie_id = :id")
    fun getCastByMovieId(id: Int): Flow<List<CastAndCrewEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieReview(reviewList: List<ReviewEntity>)

    @Query("SELECT * FROM review_table WHERE movie_id = :id")
    fun getMovieReviewById(id: Int): Flow<List<ReviewEntity>>

}