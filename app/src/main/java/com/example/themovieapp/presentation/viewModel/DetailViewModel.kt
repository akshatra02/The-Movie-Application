package com.example.themovieapp.presentation.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovieapp.data.source.remote.dto.favorites.FavouriteBody
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.domain.usecase.FavouriteMoviesUseCase
import com.example.themovieapp.domain.usecase.GetMovieByIdUseCase
import com.example.themovieapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
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

    val movieId = savedStateHandle.get<Int>("movieId")

    init {
        getMoviesById(movieId ?: -1)
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
            val favouriteBody =
                FavouriteBody(
                    favorite = movie.isFavourite,
                    media_id = movie.id,
                    media_type = "movie"
                )
            favouriteMoviesUseCase.addMovieToFavouriteStream(favouriteBody).collectLatest { result ->
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
    val movieDetails: Movie? = null,
    val isLoading: Boolean = true,
    val isFavourite: Boolean = false
)