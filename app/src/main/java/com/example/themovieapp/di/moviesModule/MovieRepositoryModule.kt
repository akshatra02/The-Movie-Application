package com.example.themovieapp.di.moviesModule

import com.example.themovieapp.data.repository.MovieRepositoryImpl
import com.example.themovieapp.data.source.local.room.MovieEntityDao
import com.example.themovieapp.data.source.remote.MoviesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MovieRepositoryModule {

    @Provides
    @Singleton
    fun provideMovieRepositoryImpl(
        moviesApi: MoviesApi,
        movieEntityDao: MovieEntityDao
    ): MovieRepositoryImpl = MovieRepositoryImpl(movieEntityDao,moviesApi)
}