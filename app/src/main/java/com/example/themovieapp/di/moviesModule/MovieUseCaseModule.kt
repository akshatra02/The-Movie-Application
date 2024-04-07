package com.example.themovieapp.di.moviesModule

import com.example.themovieapp.data.repository.MovieRepositoryImpl
import com.example.themovieapp.domain.repository.MovieRepository
import com.example.themovieapp.domain.usecase.GetMovieList
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MovieUseCaseModule {

    @Provides
    @Singleton
    fun providesMovieUseCase(movieRepository: MovieRepository): GetMovieList = GetMovieList(movieRepository)
}