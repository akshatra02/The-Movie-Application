package com.example.themovieapp.presentation.viewModel

import android.view.View
import androidx.compose.runtime.MutableState
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

    private val _topRatedUiState = mutableStateOf(MovieUiState())
    val topRatedUiState: State<MovieUiState> = _topRatedUiState

    private val _upcomingUiState = mutableStateOf(MovieUiState())
    val upcomingUiState: State<MovieUiState> = _upcomingUiState

    private val _nowPlayingUiState = mutableStateOf(MovieUiState())
    val nowPlayingUiState: State<MovieUiState> = _nowPlayingUiState

    private val _popularUiState = mutableStateOf(MovieUiState())
    val popularUiState: State<MovieUiState> = _popularUiState
    var poster: String? = null


    init {
        getMovies(Category.NOW_PLAYING, _nowPlayingUiState)
        getMovies(Category.UPCOMING, _upcomingUiState)
        getMovies(Category.TOP_RATED, _topRatedUiState)
        getMovies(Category.POPULAR, _popularUiState)
    }

     fun getMovies(category: String, uiState: MutableState<MovieUiState>) {
        viewModelScope.launch {
            getMovieList(category, popularUiState.value.page).onEach { result ->
                poster = result.data?.random()?.poster_path
                when (result) {
                    is Resource.Error -> {
                        uiState.value = uiState.value.copy(
                            movieList = result.data ?: emptyList(), isLoading = false
                        )
                    }

                    is Resource.Loading -> {
                        uiState.value = uiState.value.copy(
                            movieList = result.data ?: emptyList(), isLoading = true
                        )
                    }

                    is Resource.Success -> {
                        uiState.value = uiState.value.copy(
                            movieList = result.data ?: emptyList(),
                            isLoading = false,
                            page = uiState.value.page + 1,
                        )
                    }
                }

            }.launchIn(this)


        }
    }
}

data class MovieUiState(
    val movieList: List<Movie> = emptyList(),
    val page: Int = 1,
    val isLoading: Boolean = true,

    )