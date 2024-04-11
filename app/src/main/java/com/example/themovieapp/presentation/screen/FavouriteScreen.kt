package com.example.themovieapp.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.themovieapp.presentation.components.BottomTab
import com.example.themovieapp.presentation.components.MovieCard
import com.example.themovieapp.presentation.navigation.Screen
import com.example.themovieapp.presentation.viewModel.FavouriteViewModel
import com.example.themovieapp.utils.TabPage
import com.example.themovieapp.utils.toDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouriteScreen(navController: NavController, viewModel: FavouriteViewModel = hiltViewModel()) {
    val favouriteUiState by viewModel.uiState.collectAsState()
    var tabPage by remember {
        mutableStateOf(TabPage.FAVOURITE)
    }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Favourite") })
        },
        bottomBar = {
            BottomAppBar {
                BottomTab(
                    navController = navController,
                    tabPage = tabPage,
                    onTabSelected = { tabPage = it })

            }
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(paddingValues)
        ) {
            items(favouriteUiState.movieList.size) { index ->
                val favouriteMovie = favouriteUiState.movieList[index]
                MovieCard(
                    title = favouriteMovie.title,
                    date = favouriteMovie.release_date,
                    photo = favouriteMovie.poster_path,
                    moreMovieDetails = {
                        navController.navigate("${Screen.DetailScreen.route}/${favouriteMovie.id}")
                    })


            }


        }
    }

}