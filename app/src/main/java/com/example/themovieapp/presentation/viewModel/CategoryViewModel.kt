package com.example.themovieapp.presentation.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovieapp.data.source.remote.Resource
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.domain.usecase.GetMovieList
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
    private val getMovieList: GetMovieList,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _movieListUiState = MutableStateFlow(MovieUiState())
    val movieListUiState = _movieListUiState.asStateFlow()

//    val category = savedStateHandle.get<String>("category")
//
//    init {
//        getMoviesByCategory(category.toString(),1)
//    }



    fun getMoviesByCategory(category: String) {
        viewModelScope.launch {
            getMovieList.getMovieList(category,movieListUiState.value.page).collectLatest { result ->
                when(result){
                    is Resource.Error -> {
                        _movieListUiState.update {
                            it.copy(isLoading = true)
                        }
                    }
                    is Resource.Loading -> {
                        _movieListUiState.update {
                            it.copy(isLoading = true)
                        }

                    }
                    is Resource.Success -> {
                        _movieListUiState.update {
                            it.copy(
                                movieList = result.data ?: emptyList(),
                                isLoading = false,
                                page = movieListUiState.value.page + 1,

                            )
                        }

                    }
                }

            }
        }
    }

}