package com.example.themovieapp.di.moviesModule

import com.example.themovieapp.domain.repository.MovieDetailsRepository
import com.example.themovieapp.domain.repository.MovieRepository
import com.example.themovieapp.domain.usecase.AddExtraMovieDetails
import com.example.themovieapp.domain.usecase.UpdateFavouriteStatusUseCase
import com.example.themovieapp.domain.usecase.GetAllMoviesUseCase
import com.example.themovieapp.domain.usecase.GetCastAndCrewUseCase
import com.example.themovieapp.domain.usecase.GetFavouriteMoviesUseCase
import com.example.themovieapp.domain.usecase.GetExtraMovieDetailsUseCase
import com.example.themovieapp.domain.usecase.GetMovieReviewUseCase
import com.example.themovieapp.domain.usecase.GetMoviesByCategoryUseCase
import com.example.themovieapp.domain.usecase.LoadMoreMoviesUseCase
import com.example.themovieapp.domain.usecase.SetInitialDefaultsUseCase
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
    fun providesGetMoviesByCategoryUseCase(movieRepository: MovieRepository): GetMoviesByCategoryUseCase =
        GetMoviesByCategoryUseCase(movieRepository)


    @Provides
    @Singleton
    fun providesGetAllMoviesUseCase(movieRepository: MovieRepository): GetAllMoviesUseCase =
        GetAllMoviesUseCase(movieRepository)

    @Provides
    @Singleton
    fun providesLoadMoreMoviesUseCase(movieDetailsRepository: MovieRepository): LoadMoreMoviesUseCase =
        LoadMoreMoviesUseCase(movieDetailsRepository)

    @Provides
    @Singleton
    fun providesSetInitialDefaultsUseCase(movieDetailsRepository: MovieRepository): SetInitialDefaultsUseCase =
        SetInitialDefaultsUseCase(movieDetailsRepository)

    @Provides
    @Singleton
    fun providesGetFavouriteMoviesUseCase(favouriteMovieRepository: MovieDetailsRepository): GetFavouriteMoviesUseCase =
        GetFavouriteMoviesUseCase(favouriteMovieRepository)

    @Provides
    @Singleton
    fun providesAddFavouriteMoviesUseCase(favouriteMovieRepository: MovieDetailsRepository): UpdateFavouriteStatusUseCase =
        UpdateFavouriteStatusUseCase(favouriteMovieRepository)

    @Provides
    @Singleton
    fun providesAddExtraMovieDetailsUseCase(movieDetailsRepository: MovieDetailsRepository): AddExtraMovieDetails =
        AddExtraMovieDetails(movieDetailsRepository)

    @Provides
    @Singleton
    fun providesGetCastAndCrewUseCase(movieDetailsRepository: MovieDetailsRepository): GetCastAndCrewUseCase =
        GetCastAndCrewUseCase(movieDetailsRepository)

    @Provides
    @Singleton
    fun providesGetExtraMovieDetailsUseCase(movieDetailsRepository: MovieDetailsRepository): GetExtraMovieDetailsUseCase =
        GetExtraMovieDetailsUseCase(movieDetailsRepository)

    @Provides
    @Singleton
    fun providesGetMovieReviewDetailsUseCase(movieDetailsRepository: MovieDetailsRepository): GetMovieReviewUseCase =
        GetMovieReviewUseCase(movieDetailsRepository)
}
