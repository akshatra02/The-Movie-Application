package com.example.themovieapp.di.moviesModule

import com.example.themovieapp.domain.repository.FavoriteMovieRepository
import com.example.themovieapp.domain.repository.MovieDetailsRepository
import com.example.themovieapp.domain.repository.MovieRepository
import com.example.themovieapp.domain.usecase.AddExtraMovieDetails
import com.example.themovieapp.domain.usecase.AddFavouriteMovieUseCase
import com.example.themovieapp.domain.usecase.GetAllMoviesUseCase
import com.example.themovieapp.domain.usecase.GetCastAndCrewUseCase
import com.example.themovieapp.domain.usecase.GetFavouriteMoviesUseCase
import com.example.themovieapp.domain.usecase.GetExtraMovieDetailsUsecase
import com.example.themovieapp.domain.usecase.GetMovieByIdUseCase
import com.example.themovieapp.domain.usecase.GetMovieReviewUseCase
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
    fun providesGetMoviesByCategoryUseCase(movieRepository: MovieRepository): GetMoviesByCategoryUseCase =
        GetMoviesByCategoryUseCase(movieRepository)


    @Provides
    @Singleton
    fun providesGetAllMoviesUseCase(movieRepository: MovieRepository): GetAllMoviesUseCase =
        GetAllMoviesUseCase(movieRepository)


    @Provides
    @Singleton
    fun providesGetMovieByIdUseCase(movieRepository: MovieRepository): GetMovieByIdUseCase =
        GetMovieByIdUseCase(movieRepository)

    @Provides
    @Singleton
    fun providesGetFavouriteMoviesUseCase(favouriteMovieRepository: FavoriteMovieRepository): GetFavouriteMoviesUseCase =
        GetFavouriteMoviesUseCase(favouriteMovieRepository)

    @Provides
    @Singleton
    fun providesAddFavouriteMoviesUseCase(favouriteMovieRepository: FavoriteMovieRepository): AddFavouriteMovieUseCase =
        AddFavouriteMovieUseCase(favouriteMovieRepository)

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
    fun providesGetExtraMovieDetailsUseCase(movieDetailsRepository: MovieDetailsRepository): GetExtraMovieDetailsUsecase =
        GetExtraMovieDetailsUsecase(movieDetailsRepository)

    @Provides
    @Singleton
    fun providesGetMovieReviewDetailsUseCase(movieDetailsRepository: MovieDetailsRepository): GetMovieReviewUseCase =
        GetMovieReviewUseCase(movieDetailsRepository)

}
