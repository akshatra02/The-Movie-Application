package com.example.themovieapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovieapp.data.source.remote.dto.favorites.FavouriteBody
import com.example.themovieapp.domain.model.CastAndCrew
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.domain.model.MovieDetailsAndExtraDetails
import com.example.themovieapp.domain.model.Review
import com.example.themovieapp.domain.usecase.AddExtraMovieDetails
import com.example.themovieapp.domain.usecase.AddFavouriteMovieUseCase
import com.example.themovieapp.domain.usecase.GetCastAndCrewUseCase
import com.example.themovieapp.domain.usecase.GetExtraMovieDetailsUsecase
import com.example.themovieapp.domain.usecase.GetMovieByIdUseCase
import com.example.themovieapp.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovieByIdUseCase: GetExtraMovieDetailsUsecase,
    private val addFavouriteMoviesUseCase: AddFavouriteMovieUseCase,
    private val addExtraMovieDetails: AddExtraMovieDetails,
    private val getMovieById: GetMovieByIdUseCase,
    private val getCastAndCrewStream: GetCastAndCrewUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _movieDetailsUiState = MutableStateFlow(MovieDetailUiState())
    val movieDetailsUiState = _movieDetailsUiState.asStateFlow()

    private val _recommendedMovieListUiState = MutableStateFlow(MovieUiState())
    val recommendedMovieListUiState = _recommendedMovieListUiState.asStateFlow()

    private val _castAndCrewListUiState = MutableStateFlow(CastAndCrewListUiState())
    val castAndCrewListUiState = _castAndCrewListUiState.asStateFlow()

    private val _reviewListUiState = MutableStateFlow(ReviewListUiState())
    val reviewListUiState = _reviewListUiState.asStateFlow()

    private val _movieAndExtraDetailUiState = MutableStateFlow(MovieAndExtraDetailsUiState())
    val movieAndExtraDetailUiState = _movieAndExtraDetailUiState.asStateFlow()


    val movieId = savedStateHandle.get<Int>("movieId")

    init {

        if (movieId != null) {
            getMovieDetails(movieId)
            getExtraMovieDetails(movieId)
        }
    }

    private fun getMovieDetails(movieId: Int) {
        _movieAndExtraDetailUiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
                val result = getMovieByIdUseCase(movieId)
                when (result) {
                    is Result.Error ->
                        _movieAndExtraDetailUiState.update {
                            it.copy(isLoading = true)
                        }

                    is Result.Loading ->
                        _movieAndExtraDetailUiState.update {
                            it.copy(isLoading = movieAndExtraDetailUiState.value.isLoading)
                        }

                    is Result.Success -> {
                        result.data?.collectLatest { result ->
                            _movieAndExtraDetailUiState.update {
                                it.copy(
                                    movieAndExtraDetails = result,
                                    extraDetailsAvailable = result?.movieId != null
                                )
                            }
                        }
                    }
            }
        }
    }

    private fun getExtraMovieDetails(movieId :Int) {
        viewModelScope.launch {
                _movieAndExtraDetailUiState.update { it.copy(isLoading = true) }
                val result = addExtraMovieDetails(movieId)
                when (result) {
                    is Result.Error -> {
                        _movieAndExtraDetailUiState.update {
                            it.copy(isLoading = true)
                        }

                    }

                    is Result.Loading -> {
                        _movieAndExtraDetailUiState.update {
                            it.copy(isLoading = movieAndExtraDetailUiState.value.isLoading)
                        }

                    }

                    is Result.Success -> {
                        val movie = result.data?.first()
                        _movieAndExtraDetailUiState.update {
                            it.copy(
                                movieAndExtraDetails = movie
                            )
                        }

                        val listOfRecommendationMovies = movie?.recommendationMoviesList
                        if (listOfRecommendationMovies != null) {
                            for (recommendedMovie in listOfRecommendationMovies) {
                                val recommendedMovieResult = getMovieById(recommendedMovie)
                                when (recommendedMovieResult) {
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
                                                movieList = recommendedMovieListUiState.value.movieList + recommendedMovieState
                                            )
                                        }
                                    }

                                }

                            }
                        }

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
                                    it.copy(isLoading = castAndCrewListUiState.value.isLoading)
                                }
                            }

                            is Result.Success -> {
                                val castAndCrew = castAndCrewResult.data?.first()
                                if (castAndCrew !=null) {
                                    _castAndCrewListUiState.update {
                                        it.copy(
                                            castAndCrew = castAndCrewListUiState.value.castAndCrew + castAndCrew,
                                            isLoading = false
                                        )
                                    }
                                }
                            }
                        }
                        Log.d("ji","ji")
                        val reviewResult = getMovieByIdUseCase.getMovieReviewStream(movieId)
                        when (reviewResult) {
                            is Result.Error -> {
                                _reviewListUiState.update {
                                    it.copy(isLoading = true)
                                }
                            }

                            is Result.Loading -> {
                                _reviewListUiState.update {
                                    it.copy(isLoading = reviewListUiState.value.isLoading)
                                }
                            }

                            is Result.Success ->{
                                val review = reviewResult.data?.first()
                                if (review != null) {
                                    _reviewListUiState.update {
                                        it.copy(
                                            review = reviewListUiState.value.review + review,
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

    fun addMovieToFavourite(id: Int) {
        viewModelScope.launch {
            val movie = getMovieByIdUseCase(id).data?.first()
            if (movie != null) {
                val updatedMovie = movie.copy(
                    isFavourite = !movie.isFavourite
                )
                _movieAndExtraDetailUiState.update { it.copy(movieAndExtraDetails = updatedMovie)}
            }
            val favouriteBody = movie?.let {
                FavouriteBody(
                    favorite = movie.isFavourite, media_id = id, media_type = "movie"
                )
            }
            favouriteBody?.let { addFavouriteMoviesUseCase(it) }
        }
    }
}

data class MovieDetailUiState(
    val movieDetails: Movie? = null, val isLoading: Boolean = true, val isFavourite: Boolean = false
)

data class CastAndCrewListUiState(
    val castAndCrew: List<CastAndCrew> = emptyList(),
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