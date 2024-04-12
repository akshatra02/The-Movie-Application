package com.example.themovieapp.data.source.local.room.moviedetails

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.themovieapp.data.source.local.room.moviedetails.entity.CastAndCrewEntity
import com.example.themovieapp.data.source.local.room.moviedetails.entity.ExtraMovieDetailsEntity
import com.example.themovieapp.data.source.local.room.moviedetails.entity.MovieEntity
import com.example.themovieapp.data.source.local.room.moviedetails.entity.ReviewEntity

@Database(
    entities = [
        MovieEntity::class,
        ExtraMovieDetailsEntity::class,
        CastAndCrewEntity::class,
        ReviewEntity::class
    ],
    version = 1
)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun moviesDao(): MovieEntityDao

    companion object {

        @Volatile
        var INSTANCE: MovieDatabase? = null

        fun getDatabase(context: Context): MovieDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context, MovieDatabase::class.java, "movie_application"
                ).build().also {
                    INSTANCE = it
                }


            }
        }
    }
}