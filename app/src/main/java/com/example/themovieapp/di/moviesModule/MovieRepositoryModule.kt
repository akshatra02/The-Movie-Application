package com.example.themovieapp.di.moviesModule

import com.example.themovieapp.data.repository.DefaultMovieRepository
import com.example.themovieapp.domain.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MovieRepositoryModule {

    @Binds
    @Singleton
    abstract fun provideMovieRepository(
        defaultMovieRepository: DefaultMovieRepository
    ): MovieRepository
}