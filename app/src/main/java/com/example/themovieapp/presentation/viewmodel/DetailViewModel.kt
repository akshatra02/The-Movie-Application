package com.example.themovieapp.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovieapp.data.source.remote.dto.favorites.FavouriteBody
import com.example.themovieapp.domain.model.CastAndCrew
import com.example.themovieapp.domain.model.MovieDetailsAndExtraDetails
import com.example.themovieapp.domain.model.Review
import com.example.themovieapp.domain.usecase.AddExtraMovieDetails
import com.example.themovieapp.domain.usecase.GetCastAndCrewUseCase
import com.example.themovieapp.domain.usecase.GetExtraMovieDetailsUseCase
import com.example.themovieapp.domain.usecase.GetMovieReviewUseCase
import com.example.themovieapp.domain.usecase.UpdateFavouriteStatusUseCase
import com.example.themovieapp.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovieByIdUseCase: GetExtraMovieDetailsUseCase,
    private val getMovieReviewUseCase: GetMovieReviewUseCase,
    private val updateFavouriteStatusUseCase: UpdateFavouriteStatusUseCase,
    private val addExtraMovieDetails: AddExtraMovieDetails,
    private val getCastAndCrewStream: GetCastAndCrewUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _movieAndExtraDetailUiState = MutableStateFlow(MovieAndExtraDetailsUiState())
    private val _castAndCrewListUiState = MutableStateFlow(CastAndCrewListUiState())
    private val _reviewListUiState = MutableStateFlow(ReviewListUiState())
    private val _recommendedMovieListUiState = MutableStateFlow(RecommendationListUiState())
    val movieId = savedStateHandle.get<Int>("movieId")

    val uiState: StateFlow<UiState> = combine(
        _movieAndExtraDetailUiState,
        _castAndCrewListUiState,
        _reviewListUiState,
        _recommendedMovieListUiState
    ) { movieAndExtraDetailsUiState, castAndCrewListUiState, reviewListUiState, recommendedMovieListUiState ->
        UiState(
            movieAndExtraDetails = movieAndExtraDetailsUiState.movieAndExtraDetails,
            castAndCrewState = castAndCrewListUiState,
            reviewState = reviewListUiState,
            recommendationState = recommendedMovieListUiState
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        UiState()
    )

    init {
        if (movieId != null) {
            getMovieDetails(movieId)
            getExtraMovieDetails(movieId)
        }
    }

    private fun getMovieDetails(movieId: Int) {
        _movieAndExtraDetailUiState.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO)  {
            when (val result = getMovieByIdUseCase(movieId)) {
                is Result.Error ->
                    _movieAndExtraDetailUiState.update {
                        it.copy(isLoading = true)
                    }

                is Result.Loading ->
                    _movieAndExtraDetailUiState.update {
                        it.copy(isLoading = _movieAndExtraDetailUiState.value.isLoading)
                    }

                is Result.Success -> {
                    result.data?.collectLatest { movieResult ->
                        _movieAndExtraDetailUiState.update {
                            it.copy(
                                movieAndExtraDetails = movieResult,
                                extraDetailsAvailable = movieResult?.movieId != null
                            )
                        }
                        val recommendationMoviesList = movieResult?.recommendationMoviesList
                        if (recommendationMoviesList != null) {
                            for (recommendedMovie in recommendationMoviesList) {
                                when (val recommendedMovieResult =
                                    getMovieByIdUseCase(recommendedMovie)) {
                                    is Result.Error -> {
                                        _recommendedMovieListUiState.update {
                                            it.copy(isLoading = true)
                                        }
                                    }

                                    is Result.Loading -> {
                                        _recommendedMovieListUiState.update {
                                            it.copy(isLoading = _recommendedMovieListUiState.value.isLoading)
                                        }
                                    }

                                    is Result.Success -> {
                                        val recommendedMovieState =
                                            recommendedMovieResult.data?.first()
                                        _recommendedMovieListUiState.update {
                                            it.copy(
                                                movieList = _recommendedMovieListUiState.value.movieList + recommendedMovieState
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getCastAndCrew(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO)  {
            val castAndCrewResult =
                getCastAndCrewStream(movieId)
            when (castAndCrewResult) {
                is Result.Error -> {
                    _castAndCrewListUiState.update {
                        it.copy(isLoading = true)
                    }
                }

                is Result.Loading -> {
                    _castAndCrewListUiState.update {
                        it.copy(isLoading = _castAndCrewListUiState.value.isLoading)
                    }
                }

                is Result.Success -> {
                    val castAndCrew = castAndCrewResult.data?.first()
                    if (castAndCrew != null) {
                        _castAndCrewListUiState.update {
                            it.copy(
                                castAndCrew = _castAndCrewListUiState.value.castAndCrew + castAndCrew,
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getReview(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO)  {
            when (val reviewResult = getMovieReviewUseCase(movieId)) {
                is Result.Error -> {
                    _reviewListUiState.update {
                        it.copy(isLoading = true)
                    }
                }

                is Result.Loading -> {
                    _reviewListUiState.update {
                        it.copy(isLoading = _reviewListUiState.value.isLoading)
                    }
                }

                is Result.Success -> {
                    val review = reviewResult.data?.first()
                    if (review != null) {
                        _reviewListUiState.update {
                            it.copy(
                                review = _reviewListUiState.value.review + review,
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getExtraMovieDetails(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO)  {
            _movieAndExtraDetailUiState.update { it.copy(isLoading = true) }
            addExtraMovieDetails(movieId)
            getCastAndCrew(movieId)
            getReview(movieId)
        }
    }

    fun addMovieToFavourite(id: Int) {
        viewModelScope.launch(Dispatchers.IO)  {
            val movie = getMovieByIdUseCase(id).data?.first()
            val favouriteBody = movie?.let {
                FavouriteBody(
                    favorite = movie.isFavourite, mediaId = id, mediaType = "movie"
                )
            }
            val addMovieToFav = favouriteBody?.let { updateFavouriteStatusUseCase(it) }
            if (addMovieToFav == true) {
                val updatedMovie = movie.copy(
                    isFavourite = !movie.isFavourite
                )
                _movieAndExtraDetailUiState.update { it.copy(movieAndExtraDetails = updatedMovie) }
            }
        }
    }
}

data class CastAndCrewListUiState(
    val castAndCrew: List<CastAndCrew> = emptyList(),
    val isLoading: Boolean = true,
)

data class RecommendationListUiState(
    val movieList: List<MovieDetailsAndExtraDetails?> = emptyList(),
    val isLoading: Boolean = true,
)

data class ReviewListUiState(
    val review: List<Review> = emptyList(),
    val isLoading: Boolean = true,
)

data class MovieAndExtraDetailsUiState(
    val movieAndExtraDetails: MovieDetailsAndExtraDetails? = null,
    val isLoading: Boolean = true,
    val extraDetailsAvailable: Boolean = false
)

data class UiState(
    val movieAndExtraDetails: MovieDetailsAndExtraDetails? = null,
    val castAndCrewState: CastAndCrewListUiState = CastAndCrewListUiState(),
    val reviewState: ReviewListUiState = ReviewListUiState(),
    val recommendationState: RecommendationListUiState = RecommendationListUiState(),
    val isLoading: Boolean = true,
)