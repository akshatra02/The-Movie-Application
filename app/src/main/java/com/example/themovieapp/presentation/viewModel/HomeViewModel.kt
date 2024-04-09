package com.example.themovieapp.presentation.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovieapp.data.source.remote.Resource
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.domain.usecase.GetMovieList
import com.example.themovieapp.utils.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMovieList: GetMovieList
) : ViewModel() {

    private val _topRatedUiState = MutableStateFlow(MovieUiState())
    val topRatedUiState = _topRatedUiState.asStateFlow()

    private val _upcomingUiState = MutableStateFlow(MovieUiState())
    val upcomingUiState = _upcomingUiState.asStateFlow()

    private val _nowPlayingUiState = MutableStateFlow(MovieUiState())
    val nowPlayingUiState = _nowPlayingUiState.asStateFlow()

    private val _popularUiState = MutableStateFlow(MovieUiState())
    val popularUiState = _popularUiState.asStateFlow()
    var poster: String? = null


    init {
        getMovies(Category.NOW_PLAYING, _nowPlayingUiState)
        getMovies(Category.UPCOMING, _upcomingUiState)
        getMovies(Category.TOP_RATED, _topRatedUiState)
        getMovies(Category.POPULAR, _popularUiState)
    }

    private fun getMovies(category: String, uiState: MutableStateFlow<MovieUiState>) {
        viewModelScope.launch {
            getMovieList.getMovieList(category, popularUiState.value.page).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        uiState.update {
                            it.copy(
                                movieList = result.data ?: emptyList(), isLoading = false
                            )
                        }
                    }

                    is Resource.Loading -> {
                        uiState.update {
                            it.copy(
                                movieList = result.data ?: emptyList(), isLoading = true
                            )
                        }
                    }

                    is Resource.Success -> {
                        poster = result.data?.random()?.poster_path
                        uiState.update {
                            it.copy(
                                movieList = result.data ?: emptyList(),
                                isLoading = false,
                                page = uiState.value.page + 1,
                            )
                        }
                    }

                }
            }
        }
    }
}

data class MovieUiState(
    val movieList: List<Movie> = emptyList(),
    val page: Int = 1,
    val isLoading: Boolean = true,
    )
