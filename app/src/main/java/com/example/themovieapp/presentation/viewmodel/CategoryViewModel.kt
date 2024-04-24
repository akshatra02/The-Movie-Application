package com.example.themovieapp.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovieapp.domain.usecase.GetMoviesByCategoryUseCase
import com.example.themovieapp.domain.usecase.LoadMoreMoviesUseCase
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
class CategoryViewModel @Inject constructor(
    private val getMoviesByCategoryUseCase: GetMoviesByCategoryUseCase,
    private val loadMoreMoviesUseCase: LoadMoreMoviesUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _movieListUiState = MutableStateFlow(MovieUiState())
    val movieListUiState = _movieListUiState.asStateFlow()
    val category = savedStateHandle.get<String>("category")

    init {
        getMoviesByCategory(category.toString())
    }

    fun loadMore() {
        viewModelScope.launch(Dispatchers.IO) {
            loadMoreMoviesUseCase(category = category.toString())
        }
    }

    private fun getMoviesByCategory(category: String) {
        viewModelScope.launch(Dispatchers.IO)  {
            when (val result = getMoviesByCategoryUseCase(category)) {
                is Result.Error -> {
                    _movieListUiState.update {
                        it.copy(isLoading = false)
                    }
                }

                is Result.Loading -> {
                    _movieListUiState.update {
                        it.copy(isLoading = result.isLoading)
                    }
                }

                is Result.Success -> result.data?.collectLatest { movieList ->
                    _movieListUiState.update {
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