package com.example.themovieapp.presentation.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovieapp.data.source.remote.Resource
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.domain.usecase.GetMovieList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovieList: GetMovieList,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _movieDetailsUiState = MutableStateFlow(MovieDetailUiState())
    val movieDetailsUiState = _movieDetailsUiState.asStateFlow()

    val movieId = savedStateHandle.get<Int>("movieId")
    init {
        getMoviesById(movieId ?: -1)
    }

    private fun getMoviesById(id: Int) {
        viewModelScope.launch {
            _movieDetailsUiState.update {
                it.copy(isLoading = true)
            }
            getMovieList.getMovieById(id).collectLatest { result ->
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
    val isLoading: Boolean = true
)