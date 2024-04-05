package com.example.themovieapp.presentation.viewModel

import android.view.View
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovieapp.data.repository.MovieRepositoryImpl
import com.example.themovieapp.data.source.remote.Resource
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.domain.usecase.GetMovieList
import com.example.themovieapp.utils.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMovieList: GetMovieList
) : ViewModel() {

    private val _homeUiState = mutableStateOf(MovieUiState())

    val homeUiState: State<MovieUiState> = _homeUiState

    init {
        getMovies(Category.TOP_RATED)
    }

    fun getMovies(category: String) {
        viewModelScope.launch {
            getMovieList(category).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _homeUiState.value = homeUiState.value.copy(
                            movieList = result.data ?: emptyList(),
                            isLoading = false
                        )
                    }

                    is Resource.Loading -> {
                        _homeUiState.value = homeUiState.value.copy(
                            movieList = result.data ?: emptyList(),
                            isLoading = true
                        )
                    }

                    is Resource.Success -> {
                        _homeUiState.value = homeUiState.value.copy(
                            movieList = result.data ?: emptyList(),
                            isLoading = false
                        )
                    }
                }

            }.launchIn(this)

        }
    }
}

data class MovieUiState(
    val movieList: List<Movie> = emptyList(),
    val isLoading: Boolean = true,

    )