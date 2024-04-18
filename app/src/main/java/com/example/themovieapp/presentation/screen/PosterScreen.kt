package com.example.themovieapp.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.themovieapp.presentation.components.BottomTab
import com.example.themovieapp.presentation.navigation.Screen
import com.example.themovieapp.presentation.viewmodel.MovieDetailsViewModel
import com.example.themovieapp.utils.TabPage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PosterScreen(navController: NavController,
                 viewModel: MovieDetailsViewModel = hiltViewModel()
) {

    var tabPage by remember {
        mutableStateOf(TabPage.HOME)
    }
    val movieAndExtraDetailUiState = viewModel.movieAndExtraDetailUiState.collectAsState().value
    val posterList = movieAndExtraDetailUiState.movieAndExtraDetails?.postersPathList
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
                text = "Posters",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(8.dp)

            )
            ImagesGrid(imageList = posterList)
        }
    }
}