package com.example.themovieapp.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.domain.usecase.GetMoviesByCategoryUseCase
import com.example.themovieapp.domain.usecase.LoadMoreMoviesUseCase
import com.example.themovieapp.domain.usecase.SetInitialDefaultsUseCase
import com.example.themovieapp.utils.Category
import com.example.themovieapp.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMoviesByCategoryUseCase: GetMoviesByCategoryUseCase,
    private val loadMoreMoviesUseCase: LoadMoreMoviesUseCase,
    private val setInitialDefaultsUseCase: SetInitialDefaultsUseCase,
) : ViewModel() {


    private val _topRatedUiState = MutableStateFlow(MovieUiState())
    private val _upcomingUiState = MutableStateFlow(MovieUiState())
    private val _nowPlayingUiState = MutableStateFlow(MovieUiState())
    private val _popularUiState = MutableStateFlow(MovieUiState())
    private val categories = mapOf(
        Pair(Category.NOW_PLAYING, _nowPlayingUiState),
        Pair(Category.TOP_RATED, _topRatedUiState),
        Pair(Category.UPCOMING, _upcomingUiState),
        Pair(Category.POPULAR, _popularUiState)
    )
    val homeUiState: StateFlow<HomeUiState> = combine(
        _topRatedUiState, _upcomingUiState, _nowPlayingUiState, _popularUiState
    ) { topRatedUiState, upcomingUiState, nowPlayingUiState, popularUiState ->
        HomeUiState(
            topRatedUiState = topRatedUiState,
            upcomingUiState = upcomingUiState,
            nowPlayingUiState = nowPlayingUiState,
            popularUiState = popularUiState

        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())

    init {
        setInitialDefaults()
        getMovies(Category.NOW_PLAYING)
        getMovies(Category.UPCOMING)
        getMovies(Category.TOP_RATED)
        getMovies(Category.POPULAR)
    }

    private fun setInitialDefaults() {
        viewModelScope.launch(Dispatchers.IO) {
            setInitialDefaultsUseCase(
                listOf(
                    Category.NOW_PLAYING,
                    Category.UPCOMING,
                    Category.TOP_RATED,
                    Category.POPULAR
                )
            )
        }
    }

    fun loadMore(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loadMoreMoviesUseCase(category)
        }
    }

    private fun getMovies(
        category: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val _uiState: MutableStateFlow<MovieUiState>? = categories[category]
            if (_uiState != null) {
                _uiState.update { it.copy(isLoading = true) }
                val result =
                    getMoviesByCategoryUseCase(category)
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
                        result.data?.collectLatest { moviesList ->
                            _uiState.update {
                                it.copy(
                                    movieList = moviesList,
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
    val isLoading: Boolean = true,
)

data class HomeUiState(
    val topRatedUiState: MovieUiState = MovieUiState(),
    val upcomingUiState: MovieUiState = MovieUiState(),
    val nowPlayingUiState: MovieUiState = MovieUiState(),
    val popularUiState: MovieUiState = MovieUiState(),
)