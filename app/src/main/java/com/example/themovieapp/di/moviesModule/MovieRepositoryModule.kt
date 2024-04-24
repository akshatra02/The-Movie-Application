package com.example.themovieapp.di.moviesModule

import com.example.themovieapp.data.repository.DefaultMovieDetailsRepository
import com.example.themovieapp.data.repository.DefaultMovieRepository
import com.example.themovieapp.domain.repository.MovieDetailsRepository
import com.example.themovieapp.domain.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MovieRepositoryModule {

    @Binds
    @Singleton
    abstract fun provideMovieRepository(
        defaultMovieRepository: DefaultMovieRepository
    ): MovieRepository

    @Binds
    @Singleton
    abstract fun provideMovieDetailsRepository(
        defaultMovieDetailsRepository: DefaultMovieDetailsRepository
    ): MovieDetailsRepository
}