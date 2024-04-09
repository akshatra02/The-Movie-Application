package com.example.themovieapp.presentation.screen

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.themovieapp.R
import com.example.themovieapp.data.source.remote.MoviesApi
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.presentation.components.BottomTab
import com.example.themovieapp.presentation.navigation.Screen
import com.example.themovieapp.presentation.viewModel.MovieDetailsViewModel
import com.example.themovieapp.utils.TabPage
import com.example.themovieapp.utils.toDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavController,
    movieId: Int,
    viewModel: MovieDetailsViewModel = hiltViewModel()
) {
    var tabPage by remember {
        mutableStateOf(TabPage.HOME)
    }
    val movieDetailUiState = viewModel.movieDetailsUiState.collectAsState().value
    Scaffold(
        topBar = {
            TopAppBar(
                title = { movieDetailUiState.movieDetails?.let { Text(text = it.title) } },
            )
        },
        bottomBar = {
            BottomAppBar {
                BottomTab(navController = navController, tabPage = tabPage, onTabSelected = { tabPage = it})
            }

        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .verticalScroll(ScrollState(1), true)
                .padding(paddingValues),

            ) {
            movieDetailUiState.movieDetails?.let { movie ->
                Column {
                    BackDrop(movieDetails = movie, modifier = Modifier.height(500.dp))
                    MovieContent(movie = movie, onClickFavourite = {viewModel.addMovieToFavourite()}, modifier = Modifier)
                }
            }
        }
    }
}

@Composable
fun BackDrop(
    movieDetails: Movie,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
    ) {
        AsyncImage(
            model = ImageRequest
                .Builder(context = LocalContext.current)
                .data(MoviesApi.IMAGE_BASE_URL.plus(movieDetails.backdrop_path))
                .crossfade(true)
                .build(),
            alpha = 0.5f,
            error = painterResource(id = R.drawable.ic_broken_image),
            placeholder = painterResource(id = R.drawable.loading_img),
            contentScale = ContentScale.Crop,
            contentDescription = "",
            modifier = modifier.align(Alignment.TopCenter)
        )
        AsyncImage(
            model = ImageRequest
                .Builder(context = LocalContext.current)
                .data(MoviesApi.IMAGE_BASE_URL.plus(movieDetails.poster_path))
                .crossfade(true)
                .build(),

            error = painterResource(id = R.drawable.ic_broken_image),
            placeholder = painterResource(id = R.drawable.loading_img),
            contentDescription = "",
            modifier = modifier
                .size(200.dp)
                .align(Alignment.BottomCenter)
        )

    }
}

@Composable
fun MovieContent(movie: Movie,onClickFavourite: () -> Unit, modifier: Modifier) {
    val favouriteMovieIcon =
            if (movie.isFavourite) Icons.Default.Favorite
            else Icons.Default.FavoriteBorder

    Column(
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.headlineLarge,
            )
            IconButton(onClick = { onClickFavourite() }, modifier = modifier.align(Alignment.BottomEnd)) {
                Icon(imageVector = favouriteMovieIcon, contentDescription = "")
            }
        }
        Text(
            text = "${toDate(movie.release_date)} \n ${movie.genre_names}",
            style = MaterialTheme.typography.titleMedium,
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center,
            modifier = modifier
                .alpha(0.6f)
                .padding(vertical = 10.dp)
                .align(Alignment.CenterHorizontally)

        )
        Text(
            text = "Overview",
            modifier = modifier.padding(vertical = 10.dp),
            style = MaterialTheme.typography.titleLarge
        )
        Text(text = movie.overview)


    }


}