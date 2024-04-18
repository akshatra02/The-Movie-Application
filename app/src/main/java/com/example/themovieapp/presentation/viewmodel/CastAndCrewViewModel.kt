package com.example.themovieapp.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovieapp.domain.usecase.GetCastAndCrewUseCase
import com.example.themovieapp.domain.usecase.GetExtraMovieDetailsUsecase
import com.example.themovieapp.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CastAndCrewViewModel @Inject constructor(
    private val getMovieByIdUseCase: GetCastAndCrewUseCase,
    private val savedStateHandle: SavedStateHandle

) : ViewModel() {

    val movieId = savedStateHandle.get<Int>("movieId") ?: -1


    private val _castAndCrewListUiState = MutableStateFlow(CastAndCrewListUiState())
    val castAndCrewListUiState = _castAndCrewListUiState.asStateFlow()

    init {
        getCastAndCrewList()
    }

    fun getCastAndCrewList(){
        viewModelScope.launch {
            val castAndCrewResult =
                getMovieByIdUseCase(movieId)
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
        }
    }

}