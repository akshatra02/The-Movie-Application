package com.example.themovieapp.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovieapp.data.source.remote.dto.favorites.FavouriteBody
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.domain.usecase.FavouriteMoviesUseCase
import com.example.themovieapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val favouriteMoviesUseCase: FavouriteMoviesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getFavouriteMovies()
    }

    private fun getFavouriteMovies() {
        viewModelScope.launch {
            favouriteMoviesUseCase.getFavouriteMoviesStream().collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                movieList = result.data ?: emptyList(), isLoading = false
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(
                                movieList = result.data ?: emptyList(), isLoading = result.isLoading
                            )
                        }
                    }
                    is Resource.Success ->
                        result.data?.let{movieList ->
                        _uiState.update {
                            it.copy(
                                movieList = _uiState.value.movieList + movieList,
                                page = uiState.value.page + 1,
                            )
                        }
                    }
                }
            }
        }
    }
}