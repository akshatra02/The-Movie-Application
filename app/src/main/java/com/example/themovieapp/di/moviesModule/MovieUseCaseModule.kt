package com.example.themovieapp.di.moviesModule

import com.example.themovieapp.domain.repository.MovieRepository
import com.example.themovieapp.domain.usecase.FavouriteMoviesUseCase
import com.example.themovieapp.domain.usecase.GetAllMoviesUseCase
import com.example.themovieapp.domain.usecase.GetMovieByIdUseCase
import com.example.themovieapp.domain.usecase.GetMoviesByCategoryUseCase
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
    fun providesGetMoviesByCategoryUseCase(movieRepository: MovieRepository): GetMoviesByCategoryUseCase = GetMoviesByCategoryUseCase(movieRepository)

    @Provides
    @Singleton
    fun providesFavouriteMoviesUseCase(movieRepository: MovieRepository): FavouriteMoviesUseCase = FavouriteMoviesUseCase(movieRepository)

    @Provides
    @Singleton
    fun providesGetAllMoviesUseCase(movieRepository: MovieRepository): GetAllMoviesUseCase = GetAllMoviesUseCase(movieRepository)

    @Provides
    @Singleton
    fun providesGetMovieByIdUseCase(movieRepository: MovieRepository): GetMovieByIdUseCase = GetMovieByIdUseCase(movieRepository)
}