package com.example.themovieapp.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovieapp.data.source.remote.dto.extramoviedetails.ExtraMovieDetailsDto
import com.example.themovieapp.data.source.remote.dto.favorites.FavouriteBody
import com.example.themovieapp.data.source.remote.dto.movielist.MovieListDto
import com.example.themovieapp.domain.model.ExtraMovieDetails
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.domain.usecase.FavouriteMoviesUseCase
import com.example.themovieapp.domain.usecase.GetMovieByIdUseCase
import com.example.themovieapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovieByIdUseCase: GetMovieByIdUseCase,
    private val favouriteMoviesUseCase: FavouriteMoviesUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _movieDetailsUiState = MutableStateFlow(MovieDetailUiState())
    val movieDetailsUiState = _movieDetailsUiState.asStateFlow()

    private val _extraMovieDetailsUiState = MutableStateFlow(ExtraMovieDetailUiState())
    val extraMovieDetailsUiState = _extraMovieDetailsUiState.asStateFlow()

    private val _recommendedMovieListUiState = MutableStateFlow(MovieUiState())
    val recommendedMovieListUiState = _recommendedMovieListUiState.asStateFlow()


    val movieId = savedStateHandle.get<Int>("movieId")

    init {
        getMoviesById(movieId ?: -1)
        addExtraMovieDetails()
    }

    fun addExtraMovieDetails() {
        viewModelScope.launch {
            if (movieId != null) {
                getMovieByIdUseCase.addExtraMovieDetails(movieId).collectLatest { result ->
                    when (result) {
                        is Resource.Error -> {
                            _extraMovieDetailsUiState.update {
                                it.copy(isLoading = true)
                            }
                        }

                        is Resource.Loading -> {
                            _extraMovieDetailsUiState.update {
                                it.copy(isLoading = extraMovieDetailsUiState.value.isLoading)
                            }
                        }

                        is Resource.Success -> result.data?.let { movie ->
                            _extraMovieDetailsUiState.update {
                                it.copy(
                                    extraMovieDetails = movie
                                )
                            }
                            val listOfRecommendationMovies = movie.recommendationMoviesList
                            for (recommendedMovie in listOfRecommendationMovies) {
                                getMovieByIdUseCase(recommendedMovie).collectLatest { result ->
                                    when (result) {
                                        is Resource.Error -> {
                                            _recommendedMovieListUiState.update {
                                                it.copy(isLoading = true)
                                            }
                                        }

                                        is Resource.Loading -> {
                                            _recommendedMovieListUiState.update {
                                                it.copy(isLoading = _recommendedMovieListUiState.value.isLoading)
                                            }
                                        }

                                        is Resource.Success -> result.data?.let { movie ->
                                            _recommendedMovieListUiState.update {
                                                it.copy(
                                                    movieList = recommendedMovieListUiState.value.movieList + movie
                                                )
                                            }
                                        }
                                    }

                                }
                            }
                        }

                    }
                }
            }
        }
    }

    fun addMovieToFavourite() {
        viewModelScope.launch {
            val currentMovie = movieDetailsUiState.value.movieDetails
            if (currentMovie != null) {
                val updatedMovie = currentMovie.copy(
                    isFavourite = !currentMovie.isFavourite
                )
                _movieDetailsUiState.update { it.copy(movieDetails = updatedMovie) }
                // Update database with new isFavourite value (using repository function)
                updateMovieFavouriteStatus(updatedMovie)
            }
        }
    }

    fun updateMovieFavouriteStatus(movie: Movie) {
        viewModelScope.launch {
            val favouriteBody = FavouriteBody(
                favorite = movie.isFavourite, media_id = movie.id, media_type = "movie"
            )
            favouriteMoviesUseCase.addMovieToFavouriteStream(favouriteBody)
                .collectLatest { result ->
                    when (result) {
                        is Resource.Error -> {
                            _movieDetailsUiState.update {
                                it.copy(
                                    isLoading = false

                                )
                            }
                        }

                        is Resource.Loading -> {
                            _movieDetailsUiState.update {
                                it.copy(
                                    isLoading = true
                                )
                            }
                        }

                        is Resource.Success -> {
                            result.data?.let { movie ->
                                _movieDetailsUiState.update {
                                    it.copy(movieDetails = movie)
                                }
                            }
                            _movieDetailsUiState.update {
                                it.copy(
                                    isLoading = false
                                )
                            }
                        }
                    }

                }

        }
    }

    private fun getMoviesById(id: Int) {
        viewModelScope.launch {
            _movieDetailsUiState.update {
                it.copy(isLoading = true)
            }
            getMovieByIdUseCase(id).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _movieDetailsUiState.update {
                            it.copy(
                                isLoading = false

                            )
                        }
                    }

                    is Resource.Loading -> {
                        _movieDetailsUiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }

                    is Resource.Success -> {
                        result.data?.let { movie ->
                            _movieDetailsUiState.update {
                                it.copy(movieDetails = movie)
                            }
                        }
                        _movieDetailsUiState.update {
                            it.copy(
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }
}

data class MovieDetailUiState(
    val movieDetails: Movie? = null, val isLoading: Boolean = true, val isFavourite: Boolean = false
)

data class ExtraMovieDetailUiState(
    val extraMovieDetails: ExtraMovieDetails? = null,
    val isLoading: Boolean = true,
)