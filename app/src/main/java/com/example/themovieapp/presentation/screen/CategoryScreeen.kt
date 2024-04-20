package com.example.themovieapp.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.themovieapp.R
import com.example.themovieapp.presentation.components.Header
import com.example.themovieapp.presentation.components.cards.MovieCard
import com.example.themovieapp.presentation.components.loadingitems.LoadingMovieCard
import com.example.themovieapp.presentation.navigation.Screen
import com.example.themovieapp.presentation.viewmodel.CategoryViewModel
import com.example.themovieapp.utils.TabPage


@Composable
fun CategoryScreen(
    navController: NavController, viewModel: CategoryViewModel = hiltViewModel(), category: String
) {
    val categoryUiState by viewModel.movieListUiState.collectAsState()
    val tabPage by remember {
        mutableStateOf(TabPage.HOME)
    }
    val categoryHeading = when (category) {
        "upcoming" -> R.string.upcoming
        "now_playing" -> R.string.now_playing
        "top_rated" -> R.string.top_rated
        else -> R.string.popular
    }
    Header(title = categoryHeading, navController = navController, tabPage = tabPage)

    { paddingValues ->
        val movies = categoryUiState.movieList
        val isLoading = categoryUiState.isLoading
        if (movies.isEmpty() || isLoading) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2), modifier = Modifier.padding(paddingValues)
            ) {
                items(2) {
                    LoadingMovieCard()
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                items(movies.size) { i ->
                    val movie = categoryUiState.movieList[i]
                    if (movie != null) {
                        MovieCard(title = movie.title,
                            date = movie.releaseDate,
                            rating = movie.voteAverage,

                            photo = movie.posterPath,
                            moreMovieDetails = {
                                navController.navigate("${Screen.DetailScreen.route}/${movie.id}/${tabPage.name}")
                            })

                        if (i == movies.lastIndex && !isLoading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(paddingValues),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                            LaunchedEffect(movies) {
                                viewModel.loadMore()
                            }
                        }
                    }
                }
            }

        }

    }
}