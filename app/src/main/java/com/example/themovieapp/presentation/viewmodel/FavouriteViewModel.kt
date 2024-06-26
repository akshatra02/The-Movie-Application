package com.example.themovieapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovieapp.domain.usecase.GetFavouriteMoviesUseCase
import com.example.themovieapp.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val getFavouriteMoviesUseCase: GetFavouriteMoviesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getFavouriteMovies()
    }

    private fun getFavouriteMovies() {
        viewModelScope.launch(Dispatchers.IO)  {
            val result = getFavouriteMoviesUseCase()
            when (result) {
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                }

                is Result.Loading -> {
                    _uiState.update {
                        it.copy(
                            isLoading = result.isLoading
                        )
                    }
                }

                is Result.Success ->
                    result.data?.collectLatest { movieList ->
                        _uiState.update {
                            it.copy(
                                movieList = movieList,
                                isLoading = false
                            )
                        }
                    }
            }
        }
    }
}