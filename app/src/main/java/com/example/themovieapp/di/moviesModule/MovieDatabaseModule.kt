package com.example.themovieapp.di.moviesModule

import android.app.Application
import com.example.themovieapp.data.source.local.room.MovieDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MovieDatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application) = MovieDatabase.getDatabase(application)

    @Provides
    @Singleton
    fun provideMoviesDao(database: MovieDatabase) = database.moviesDao()
}