package com.example.themovieapp.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.domain.usecase.GetMoviesByCategoryUseCase
import com.example.themovieapp.utils.Category
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
class HomeViewModel @Inject constructor(
    private val getMoviesByCategoryUseCase: GetMoviesByCategoryUseCase
) : ViewModel() {


    private val _topRatedUiState = MutableStateFlow(MovieUiState())
    val topRatedUiState = _topRatedUiState.asStateFlow()

    private val _upcomingUiState = MutableStateFlow(MovieUiState())
    val upcomingUiState = _upcomingUiState.asStateFlow()

    private val _nowPlayingUiState = MutableStateFlow(MovieUiState())
    val nowPlayingUiState = _nowPlayingUiState.asStateFlow()

    private val _popularUiState = MutableStateFlow(MovieUiState())
    val popularUiState = _popularUiState.asStateFlow()

    private val categories = mapOf(
        Pair(Category.NOW_PLAYING, _nowPlayingUiState),
        Pair(Category.TOP_RATED, _topRatedUiState),
        Pair(Category.UPCOMING, _upcomingUiState),
        Pair(Category.POPULAR, _popularUiState)
    )

    init {
       getMovies(Category.NOW_PLAYING, false)
        getMovies(Category.UPCOMING, false)
        getMovies(Category.TOP_RATED, false)
        getMovies(Category.POPULAR, false)
    }

    fun loadMore(category: String) {
        categories[category]?.update { it.copy(isLoading = true) }
        getMovies(category, forceFetchFromRemote = true)
        categories[category]?.update { it.copy(isLoading = false) }
    }

    private fun getMovies(
        category: String, forceFetchFromRemote: Boolean,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val _uiState :  MutableStateFlow<MovieUiState>? = categories[category]

            if (_uiState != null) {
                _uiState.update { it.copy(isLoading = true) }

                val result =
                    getMoviesByCategoryUseCase(category, forceFetchFromRemote, _uiState.value.page)

                when (result) {
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false
                            )
                        }
                        return@launch

                    }

                    is Result.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = result.isLoading
                            )
                        }
                        return@launch

                    }

                    is Result.Success -> {
                        result.data?.collectLatest{ moviesList ->
                            _uiState.update {
                                it.copy(
                                    movieList = _uiState.value.movieList + moviesList,
                                    page = _uiState.value.page + 1,
                                    isLoading = false
                                )
                            }
                        }
                    }

                }
            }
        }
    }
}

data class MovieUiState(
    val movieList: List<Movie?> = emptyList(),
    val page: Int = 1,
    val isLoading: Boolean = true,
)
