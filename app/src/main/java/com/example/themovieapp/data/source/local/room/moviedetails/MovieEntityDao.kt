package com.example.themovieapp.data.source.local.room.moviedetails

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.themovieapp.data.source.local.room.moviedetails.entity.CastAndCrewEntity
import com.example.themovieapp.data.source.local.room.moviedetails.entity.ExtraMovieDetailsEntity
import com.example.themovieapp.data.source.local.room.moviedetails.entity.MovieDetailsAndExtraDetailsData
import com.example.themovieapp.data.source.local.room.moviedetails.entity.MovieEntity
import com.example.themovieapp.data.source.local.room.moviedetails.entity.ReviewEntity

@Dao
interface MovieEntityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieList(movie: List<MovieEntity>)

    @Upsert
    suspend fun upsertMovie(movie: MovieEntity)

    @Query("SELECT * FROM movie_table WHERE category = :category")
    suspend fun getMovieListByCategory(category: String): List<MovieEntity>
    @Query("SELECT * FROM movie_table WHERE id = :id")
    suspend fun getMovieById(id : Int): MovieEntity?

    @Query("SELECT * FROM movie_table")
    suspend fun getAllMovies():List<MovieEntity>

    @Query("SELECT * FROM movie_table WHERE is_favourite = 1")
    suspend fun getFavouriteMovies():List<MovieEntity>

    @Update
    suspend fun updateMovie(movieEntity: MovieEntity): Int

    @Upsert
    suspend fun upsertExtraMovieDetails(extraMovieDetailsEntity: ExtraMovieDetailsEntity)

    @Upsert
    suspend fun insertCastAndCrew(castAndCrewEntity: CastAndCrewEntity)

    @Query("SELECT * FROM cast_and_crew_table WHERE movie_id = :id")
    suspend fun getCastByMovieId(id: Int):List<CastAndCrewEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieReview(reviewList: List<ReviewEntity>)

    @Query("SELECT * FROM review_table WHERE movie_id = :id")
    suspend fun getMovieReviewById(id: Int):List<ReviewEntity>

@Query("""SELECT movie.*, extradetails.* 
FROM movie_table AS movie 
LEFT JOIN EXTRA_MOVIE_DETAILS_TABLE AS extradetails 
ON movie.id = extradetails.movie_id
WHERE movie.id = :id
""")
suspend fun getMovieDetailsAndExtraById(id: Int): MovieDetailsAndExtraDetailsData?


}