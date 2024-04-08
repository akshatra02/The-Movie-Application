package com.example.themovieapp.presentation.screen

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.themovieapp.presentation.navigation.Screen
import com.example.themovieapp.presentation.navigation.Screen.CategoryScreen.route
import com.example.themovieapp.presentation.viewModel.CategoryViewModel
import com.example.themovieapp.presentation.viewModel.HomeViewModel
import com.example.themovieapp.presentation.viewModel.MovieUiState
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(navController: NavController, viewModel: CategoryViewModel = hiltViewModel(), category: String) {
    val categoryUiState  = viewModel.movieListUiState.collectAsState()

    Scaffold (
        topBar = {
            TopAppBar(title = { Text(text = category) },
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
        }
    ){paddingValues ->
        LaunchedEffect(Unit) {
            viewModel.getMoviesByCategory (category)
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
            ) {
            items(categoryUiState.value.movieList.size){i ->
                val movie = categoryUiState.value.movieList[i]
                CardImage(title = movie.title, date = movie.release_date, photo = movie.poster_path, moreMovieDetails = {
                    navController.navigate("${Screen.DetailScreen.route}/${movie.id}")
                })

                if (i >= categoryUiState.value.movieList.size - 1 && !categoryUiState.value.isLoading){
                    viewModel.getMoviesByCategory(category)
                }
            }
        }

    }

}