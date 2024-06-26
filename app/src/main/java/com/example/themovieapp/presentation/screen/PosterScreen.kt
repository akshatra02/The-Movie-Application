package com.example.themovieapp.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.themovieapp.presentation.components.Header
import com.example.themovieapp.presentation.navigation.Screen
import com.example.themovieapp.presentation.viewmodel.MovieDetailsViewModel
import com.example.themovieapp.utils.TabPage

@Composable
fun PosterScreen(
    navController: NavController,
    viewModel: MovieDetailsViewModel = hiltViewModel()
) {
    val tabPage by remember {
        mutableStateOf(TabPage.HOME)
    }
    val uiState by viewModel.uiState.collectAsState()
    val posterList = uiState.movieAndExtraDetails?.postersPathList
    val title = uiState.movieAndExtraDetails?.title ?: ""
    val movieId = uiState.movieAndExtraDetails?.id ?: -1
    Header(title = title, navController = navController, navigateOnClick = {
        navController.navigate("${Screen.DetailScreen.route}/${movieId}/${tabPage.name}") {
            popUpTo("${Screen.DetailScreen.route}/${movieId}/${tabPage.name}") {
                inclusive = true
            }
        }
    }, tabPage = tabPage, showIcon = false, showBackButton = true)
    { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Text(
                text = "Posters",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(8.dp)

            )
            if (posterList != null) {
                ImagesGrid(imageList = posterList)
            }
        }
    }
}