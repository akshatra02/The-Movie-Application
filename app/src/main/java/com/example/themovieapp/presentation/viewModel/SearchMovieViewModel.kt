package com.example.themovieapp.presentation.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.domain.usecase.GetMovieList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchMovieViewModel @Inject constructor(
    private val getMovieList: GetMovieList
) : ViewModel() {
    var searchQuery by mutableStateOf("")
        private set
//    fun getAllMovies(){
//        viewModelScope.launch {
//           emit(getMovieList.getAllMovies())
//
//        }
//    }
    val movieFlow = getMovieList.getAllMovies()

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