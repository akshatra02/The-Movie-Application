package com.example.themovieapp.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovieapp.domain.usecase.GetMoviesByCategoryUseCase
import com.example.themovieapp.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getMoviesByCategoryUseCase: GetMoviesByCategoryUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _movieListUiState = MutableStateFlow(MovieUiState())
    val movieListUiState = _movieListUiState.asStateFlow()

    val category = savedStateHandle.get<String>("category")

    init {
        getMoviesByCategory(category.toString(), forceFetchFromRemote = false)
    }

    fun loadMore() {
        _movieListUiState.update { it.copy(isLoading = true) }
        getMoviesByCategory(category = category.toString(), forceFetchFromRemote = true)
        _movieListUiState.update { it.copy(isLoading = false) }

    }


    private fun getMoviesByCategory(category: String, forceFetchFromRemote: Boolean) {
        viewModelScope.launch {

            val result = getMoviesByCategoryUseCase(
                category,
                forceFetchFromRemote,
                movieListUiState.value.page
            )
            when (result) {
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

                is Result.Success ->
                    result.data?.collectLatest{ movieList ->
                        _movieListUiState.update {
                            it.copy(
                                movieList = movieListUiState.value.movieList + movieList,
                                page = movieListUiState.value.page + 1,
                                isLoading = false

                            )
                        }

                    }
            }

        }
    }
}