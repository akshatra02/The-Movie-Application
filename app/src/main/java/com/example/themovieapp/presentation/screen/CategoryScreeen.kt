package com.example.themovieapp.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.themovieapp.R
import com.example.themovieapp.presentation.components.BottomTab
import com.example.themovieapp.presentation.components.loadingitems.LoadingMovieCard
import com.example.themovieapp.presentation.components.cards.MovieCard
import com.example.themovieapp.presentation.navigation.Screen
import com.example.themovieapp.presentation.viewmodel.CategoryViewModel
import com.example.themovieapp.utils.TabPage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    navController: NavController,
    viewModel: CategoryViewModel = hiltViewModel(),
    category: String
) {
    val categoryUiState by viewModel.movieListUiState.collectAsState()
    var tabPage by remember {
        mutableStateOf(TabPage.HOME)
    }
    val categoryHeading =
        when(category){
            "upcoming" -> R.string.upcoming
            "now_playing" -> R.string.now_playing
            "top_rated" -> R.string.top_rated
            else -> R.string.popular
        }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(2.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = categoryHeading), style = MaterialTheme.typography.headlineSmall
                    )
                } },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(Screen.HomeScreen.route) {
                            popUpTo(Screen.HomeScreen.route) {
                                inclusive = true
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                BottomTab(
                    navController = navController,
                    currentPage = tabPage,
                    onTabSelected = { tabPage = it })
            }

        }
    ) { paddingValues ->
        if (categoryUiState.movieList.isEmpty() || categoryUiState.isLoading) {
            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
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
                items(categoryUiState.movieList.size) { i ->
                    val movie = categoryUiState.movieList[i]
                    if (movie != null) {
                        MovieCard(
                            title = movie.title,
                            date = movie.releaseDate,
                            rating = movie.voteAverage,

                            photo = movie.posterPath,
                            moreMovieDetails = {
                                navController.navigate("${Screen.DetailScreen.route}/${movie.id}")
                            })

                        if (i >= categoryUiState.movieList.size - 1 && !categoryUiState.isLoading) {
                            viewModel.loadMore()
                        } else if (i >= categoryUiState.movieList.size - 1 && categoryUiState.isLoading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(paddingValues),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }

                        }
                    }
                }
            }

        }

    }
}