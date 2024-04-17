package com.example.themovieapp.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.themovieapp.R
import com.example.themovieapp.presentation.components.BottomTab
import com.example.themovieapp.presentation.components.MovieCard
import com.example.themovieapp.presentation.navigation.Screen
import com.example.themovieapp.presentation.viewmodel.FavouriteViewModel
import com.example.themovieapp.utils.TabPage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouriteScreen(navController: NavController, viewModel: FavouriteViewModel = hiltViewModel()) {
    val favouriteUiState by viewModel.uiState.collectAsState()
    var tabPage by remember {
        mutableStateOf(TabPage.FAVOURITE)
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
                    Image(painter = painterResource(R.drawable.movie_icon), contentDescription = "", contentScale = ContentScale.Crop, modifier = Modifier
                        .size(36.dp)
                        .clip(
                            CircleShape
                        ))
                    Text(
                        text = stringResource(R.string.favourite), style = MaterialTheme.typography.headlineMedium
                    )
                }})
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
                if (favouriteMovie != null){
                MovieCard(
                    title = favouriteMovie.title,
                    date = favouriteMovie.releaseDate,
                    rating = favouriteMovie.voteAverage,

                    photo = favouriteMovie.posterPath,
                    moreMovieDetails = {
                        navController.navigate("${Screen.DetailScreen.route}/${favouriteMovie.id}")
                    })


            }}


        }
    }

}