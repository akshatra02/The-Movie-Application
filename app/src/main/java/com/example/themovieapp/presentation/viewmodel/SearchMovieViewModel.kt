package com.example.themovieapp.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.domain.usecase.GetAllMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SearchMovieViewModel @Inject constructor(
    getAllMoviesUseCase: GetAllMoviesUseCase
) : ViewModel() {
    var searchQuery by mutableStateOf("")
        private set
    private val movieFlow = getAllMoviesUseCase()

    val searchResults: StateFlow<List<Movie>> =
        snapshotFlow { searchQuery }
            .combine(movieFlow) { searchQuery, movies ->
                when {
                    searchQuery.isNotEmpty() -> movies.filter { movie ->
                        movie.title.contains(searchQuery, true)
                    }

                    else -> movies
                }
            }.stateIn(
                scope = viewModelScope,
                initialValue = emptyList(),
                started = SharingStarted.WhileSubscribed(5_000)

            )

    fun onSearchQueryChange(newQuery: String) {
        searchQuery = newQuery
    }

}