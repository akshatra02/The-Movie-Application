package com.example.themovieapp.presentation.screen

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.themovieapp.R
import com.example.themovieapp.data.source.remote.MoviesApi
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.presentation.navigation.Screen
import com.example.themovieapp.presentation.viewModel.MovieDetailsViewModel
import com.example.themovieapp.utils.toDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavController,
    movieId: Int,
    viewModel: MovieDetailsViewModel = hiltViewModel()
) {
    val movieDetailUiState = viewModel.movieDetailsUiState.collectAsState().value
    Scaffold (
        topBar = {
            TopAppBar(title = { movieDetailUiState.movieDetails?.let { Text(text = it.title) } },
            )
        }
    ){paddingValues ->
    Column(
        modifier = Modifier.verticalScroll(ScrollState(1), true)
            .padding(paddingValues),

    ) {
        movieDetailUiState.movieDetails?.let { movie ->
            Column {
                BackDrop(movieDetails = movie, modifier = Modifier.height(500.dp))
                MovieContent(movie = movie, modifier = Modifier)
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
            error = painterResource(id = R.drawable.ic_launcher_background),
            placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
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

            error = painterResource(id = R.drawable.ic_launcher_background),
            placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "",
            modifier = modifier
                .size(200.dp)
                .align(Alignment.BottomCenter)
        )

    }
}

@Composable
fun MovieContent(movie: Movie, modifier: Modifier) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = movie.title,
            style = MaterialTheme.typography.headlineLarge,
            modifier = modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = "${toDate(movie.release_date)} ● ${movie.genre_ids}",
            style = MaterialTheme.typography.titleMedium,
            modifier = modifier.alpha(0.5f).padding(vertical = 10.dp)

        )
        Text(text = "Overview", modifier = modifier.padding(vertical = 10.dp), style = MaterialTheme.typography.titleLarge)
        Text(text = movie.overview)
        

    }


}