package com.example.themovieapp.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.themovieapp.R
import com.example.themovieapp.presentation.components.Header
import com.example.themovieapp.presentation.navigation.Screen
import com.example.themovieapp.presentation.viewmodel.MovieDetailsViewModel
import com.example.themovieapp.utils.IMAGE_BASE_URL
import com.example.themovieapp.utils.TabPage

@Composable
fun BackdropScreen(
    navController: NavController,
    viewModel: MovieDetailsViewModel = hiltViewModel()
) {
    val tabPage by remember {
        mutableStateOf(TabPage.HOME)
    }
    val uiState by viewModel.uiState.collectAsState()
    val movieAndExtraDetailUiState = uiState.movieAndExtraDetails
    val backdropsPathList = movieAndExtraDetailUiState?.backdropsPathList
    val title = movieAndExtraDetailUiState?.title ?: ""
    val movieId = movieAndExtraDetailUiState?.id ?: -1
    Header(title = title,
        navController = navController,
        showIcon = false,
        showBackButton = true,
        tabPage = tabPage,
        navigateOnClick = {
            navController.navigate("${Screen.DetailScreen.route}/${movieId}/${tabPage.name}") {
                popUpTo("${Screen.DetailScreen.route}/${movieId}/${tabPage.name}") {
                    inclusive = true
                }
            }
        })
    { paddingValues ->
        if (backdropsPathList != null) {
            Column(modifier = Modifier.padding(paddingValues)) {
                Text(
                    text = "Backdrops",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(8.dp)
                )
                ImagesGrid(backdropsPathList)
            }
        }
    }
}

@Composable
fun ImagesGrid(imageList: List<String>) {
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(imageList.size) { index ->
            val imagesPath = imageList[index]
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(IMAGE_BASE_URL.plus(imagesPath))
                    .crossfade(true)
                    .build(),
                error = painterResource(id = R.drawable.ic_broken_image),
                contentDescription = "hello",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            )
        }
    }
}