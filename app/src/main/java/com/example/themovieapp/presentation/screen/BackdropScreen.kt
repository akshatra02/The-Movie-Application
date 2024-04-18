package com.example.themovieapp.presentation.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.themovieapp.R
import com.example.themovieapp.data.source.remote.MoviesApi
import com.example.themovieapp.presentation.components.BottomTab
import com.example.themovieapp.presentation.navigation.Screen
import com.example.themovieapp.presentation.viewmodel.MovieDetailsViewModel
import com.example.themovieapp.utils.TabPage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackdropScreen(navController: NavController,
                   viewModel: MovieDetailsViewModel = hiltViewModel()
) {

    var tabPage by remember {
        mutableStateOf(TabPage.HOME)
    }
    val movieAndExtraDetailUiState = viewModel.movieAndExtraDetailUiState.collectAsState().value
    val backdropsPathList = movieAndExtraDetailUiState.movieAndExtraDetails?.backdropsPathList
   Log.d("lo",backdropsPathList.toString())
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(2.dp)
                        .fillMaxWidth()
                ) {
                    movieAndExtraDetailUiState.movieAndExtraDetails?.let {
                        Text(
                            text = it.title,
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }
            },
            navigationIcon = {
                IconButton(onClick = {
                    navController.navigate("${Screen.DetailScreen.route}/${movieAndExtraDetailUiState.movieAndExtraDetails?.id ?: -1}") {
                        popUpTo("${Screen.DetailScreen.route}/${movieAndExtraDetailUiState.movieAndExtraDetails?.id ?: -1}") {
                            inclusive = true
                        }
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = ""
                    )
                }
            }
        )
    }, bottomBar = {
        BottomAppBar {
            BottomTab(navController = navController,
                currentPage = tabPage,
                onTabSelected = { tabPage = it })
        }

    }) { paddingValues ->
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

@Composable
fun ImagesGrid(imageList : List<String>?) {
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        if (imageList != null) {
            items(imageList.size) { index ->
                val imagesPath = imageList[index]
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(MoviesApi.IMAGE_BASE_URL.plus(imagesPath))
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


}