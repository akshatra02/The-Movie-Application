package com.example.themovieapp.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.themovieapp.R
import com.example.themovieapp.presentation.components.Header
import com.example.themovieapp.presentation.components.cards.MovieCard
import com.example.themovieapp.presentation.components.loadingitems.LoadingMovieCard
import com.example.themovieapp.presentation.navigation.Screen
import com.example.themovieapp.presentation.viewmodel.FavouriteViewModel
import com.example.themovieapp.utils.TabPage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouriteScreen(navController: NavController, viewModel: FavouriteViewModel = hiltViewModel()) {
    val favouriteUiState by viewModel.uiState.collectAsState()
    val tabPage by remember {
        mutableStateOf(TabPage.FAVOURITE)
    }
    val isEmpty by remember {
        derivedStateOf { favouriteUiState.movieList.isEmpty() }
    }
    val isLoading by remember {
        derivedStateOf { favouriteUiState.isLoading }
    }
    Header(title = R.string.favourite, navController = navController, tabPage = tabPage)

    { paddingValues ->
        if (isLoading){
            LazyVerticalGrid(columns = GridCells.Fixed(2),modifier = Modifier.padding(paddingValues)) {
                items(2) {
                    LoadingMovieCard()
                }
            }
        }
        else if( isEmpty){
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = stringResource(R.string.no_movies_found),
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "You haven't added any movies to your favourite.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        else{
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(paddingValues)
        ) {
            items(items = favouriteUiState.movieList) {favouriteMovie ->
                if (favouriteMovie != null){
                MovieCard(
                    title = favouriteMovie.title,
                    date = favouriteMovie.releaseDate,
                    rating = favouriteMovie.voteAverage,
                    photo = favouriteMovie.posterPath,
                    moreMovieDetails = {
                        navController.navigate("${Screen.DetailScreen.route}/${favouriteMovie.id}/${tabPage.name}")
                    })


            }}


        }
    }

}}