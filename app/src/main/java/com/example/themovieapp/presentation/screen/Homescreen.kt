package com.example.themovieapp.presentation.screen

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.themovieapp.R
import com.example.themovieapp.data.source.remote.MoviesApi
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.presentation.navigation.Screen
import com.example.themovieapp.presentation.viewModel.HomeViewModel
import com.example.themovieapp.utils.Category
import com.example.themovieapp.utils.toDate
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "Movies",
                    style = MaterialTheme.typography.headlineMedium
                )
            })
        }
    ) { paddingValue ->
        Column(
            modifier = Modifier.padding(paddingValue)

        ) {
            LazyColumn(
            ) {
                val categories = listOf(
                    Triple(R.string.now_playing, viewModel.nowPlayingUiState.value.movieList,Category.NOW_PLAYING),
                    Triple(R.string.upcoming, viewModel.upcomingUiState.value.movieList,Category.UPCOMING),
                    Triple(R.string.top_rated, viewModel.topRatedUiState.value.movieList,Category.TOP_RATED),
                    Triple(R.string.popular, viewModel.popularUiState.value.movieList,Category.POPULAR)
                )
                item {
                    BackgroundWithTextAndButton(viewModel.poster.toString(), "Search", {})

                }
                categories.forEach { (titleResId, movieList, category) ->
                    item {
                        MovieLazyRow(
                            title = titleResId,
                            movieList = movieList,
                            categoryMoreMovies = {navController.navigate("${Screen.CategoryScreen.route}/${category}")}
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BackgroundWithTextAndButton(
    background: String,
    buttonText: String,
    onButtonClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomStart
    ) {
        // Background Image
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(MoviesApi.IMAGE_BASE_URL.plus(background))
                .crossfade(true)
                .build(),
            error = painterResource(id = R.drawable.ic_launcher_background),
            placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
            contentScale = ContentScale.FillWidth,
            alpha = .2f,
            contentDescription = "hello",
            modifier = Modifier
                .fillMaxSize()
                .height(400.dp)
        )

        // Text and Button
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            Text(
                text = "Welcome.\n" +
                        "Millions of movies, TV shows and people to discover. Explore now.",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text(text = "Search for movies...")},
                    shape = RoundedCornerShape(16.dp)
                )
                Button(
                    onClick = onButtonClick,
                    modifier = Modifier,
                    shape = RoundedCornerShape(16.dp)

                ) {
                    Text(text = buttonText)
                }
            }
        }
    }
}

@Composable
fun MovieLazyRow(
    @StringRes title: Int,
    categoryMoreMovies: () -> Unit = {},
    movieList: List<Movie>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            Text(
                text = stringResource(title),
                style = MaterialTheme.typography.headlineSmall,
                modifier = modifier.padding(start = 5.dp)
            )
            IconButton(modifier = Modifier, onClick = { categoryMoreMovies() }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "more"
                )
            }
        }
        LazyRow {
            items(movieList.size) { i ->
                val currentUiState = movieList.get(i)
                CardImage(
                    title = currentUiState.title,
                    date = currentUiState.release_date,
                    photo = currentUiState.poster_path
                )
            }
        }
    }
}

@Composable
fun CardImage(title: String, date: String, photo: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .width(250.dp)
            .height(400.dp)
            .padding(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {

        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(MoviesApi.IMAGE_BASE_URL.plus(photo))
                .crossfade(true)
                .build(),
            error = painterResource(id = R.drawable.ic_launcher_background),
            placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
//            contentScale = ContentScale,
            contentDescription = "hello",
            modifier = Modifier
                .fillMaxWidth()
                .size(300.dp)
        )
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title, style = MaterialTheme.typography.titleLarge,
                modifier = modifier.padding(start = 10.dp)
            )
            Text(
                text = toDate(date),
                modifier = modifier.padding(start = 10.dp)
            )
        }

    }
}