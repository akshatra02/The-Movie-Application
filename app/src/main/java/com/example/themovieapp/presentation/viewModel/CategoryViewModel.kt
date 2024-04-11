package com.example.themovieapp.presentation.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovieapp.domain.usecase.GetMoviesByCategoryUseCase
import com.example.themovieapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Locale.Category
import javax.inject.Inject
@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getMoviesByCategoryUseCase: GetMoviesByCategoryUseCase,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _movieListUiState = MutableStateFlow(MovieUiState())
    val movieListUiState = _movieListUiState.asStateFlow()

    val category = savedStateHandle.get<String>("category")

    init {
        getMoviesByCategory(category.toString(),forceFetchFromRemote = false)
    }

    fun loadMore() {
        _movieListUiState.update { it.copy(isLoading = true) }
        getMoviesByCategory(category = category.toString(),forceFetchFromRemote = true)
        _movieListUiState.update { it.copy(isLoading = false) }

    }



   private fun getMoviesByCategory(category: String,forceFetchFromRemote: Boolean) {
        viewModelScope.launch {
            getMoviesByCategoryUseCase(category, forceFetchFromRemote, movieListUiState.value.page).collectLatest { result ->
                when(result){
                    is Resource.Error -> {
                        _movieListUiState.update {
                            it.copy(isLoading = false)
                        }
                    }
                    is Resource.Loading -> {
                        _movieListUiState.update {
                            it.copy(isLoading = result.isLoading)
                        }

                    }
                    is Resource.Success ->
                        result.data?.let{movieList ->
                        _movieListUiState.update {
                            it.copy(
                                movieList = _movieListUiState.value.movieList + movieList,
                                page = _movieListUiState.value.page + 1,
                                isLoading = false

                            )
                        }

                    }
                }

            }
        }
    }

}