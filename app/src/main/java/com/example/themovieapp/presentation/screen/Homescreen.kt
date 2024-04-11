package com.example.themovieapp.presentation.screen

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Shapes
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.themovieapp.R
import com.example.themovieapp.data.source.remote.MoviesApi
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.presentation.components.BottomTab
import com.example.themovieapp.presentation.components.MovieCard
import com.example.themovieapp.presentation.navigation.Screen
import com.example.themovieapp.presentation.viewModel.HomeViewModel
import com.example.themovieapp.utils.Category
import com.example.themovieapp.utils.TabPage
import com.example.themovieapp.utils.toDate
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    val nowPlayingUiState by viewModel.nowPlayingUiState.collectAsState()
    val upcomingUiState by viewModel.upcomingUiState.collectAsState()
    val topRatedUiState by viewModel.topRatedUiState.collectAsState()
    val popularUiState by viewModel.popularUiState.collectAsState()
    var tabPage by remember {
        mutableStateOf(TabPage.HOME)
    }
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = "Movies", style = MaterialTheme.typography.headlineMedium
            )
        }, actions = {
            IconButton(onClick = { navController.navigate(Screen.SearchScreen.route) }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            }
        })
    }, bottomBar = {
        BottomAppBar {
            BottomTab(navController = navController,
                tabPage = tabPage,
                onTabSelected = { tabPage = it })
        }

    }) { paddingValue ->
        if (nowPlayingUiState.movieList.isEmpty() && topRatedUiState.movieList.isEmpty() && popularUiState.movieList.isEmpty()&& upcomingUiState.movieList.isEmpty() ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValue),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier.padding(paddingValue)

            ) {

                LazyColumn(
                ) {
                    val categories = listOf(
                        Triple(
                            R.string.now_playing, nowPlayingUiState, Category.NOW_PLAYING
                        ), Triple(
                            R.string.upcoming, upcomingUiState, Category.UPCOMING
                        ), Triple(
                            R.string.top_rated, topRatedUiState, Category.TOP_RATED
                        ), Triple(R.string.popular, popularUiState, Category.POPULAR)
                    )
                    item {
                        BackgroundWithTextAndButton(
                            navController = navController,
                            background = viewModel.poster.toString(),
                        )

                    }
                    items(items = categories) { item ->
                        val titleResId = item.first
                        val movieListUi = item.second
                        val category = item.third
                        MovieLazyRow(
                            title = titleResId,
                            movieList = movieListUi.movieList,
                            categoryMoreMovies = { navController.navigate("${Screen.CategoryScreen.route}/${category}") },
                            isLoading = movieListUi.isLoading,
                            loadMoreMovie = { viewModel.loadMore(category) },
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackgroundWithTextAndButton(
    navController: NavController,
    background: String,
) {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomStart
    ) {
        // Background Image
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(MoviesApi.IMAGE_BASE_URL.plus(background)).crossfade(true).build(),
            error = painterResource(id = R.drawable.ic_broken_image),
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
                text = "Welcome.\n" + "Millions of movies, TV shows and people to discover. Explore now.",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun MovieLazyRow(
    @StringRes title: Int,
    categoryMoreMovies: () -> Unit = {},
    isLoading: Boolean,
    loadMoreMovie : () -> Unit = {},
    movieList: List<Movie>,
    navController: NavController,
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
                    imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "more"
                )
            }
        }
        LazyRow {
            items(movieList.size) { i ->
                val currentUiState = movieList.get(i)
                MovieCard(
                    title = currentUiState.title,
                    date = currentUiState.release_date,
                    photo = currentUiState.poster_path,
                    moreMovieDetails = {
                        navController.navigate("${Screen.DetailScreen.route}/${currentUiState.id}")
                    },
                )
                if (i >= movieList.size - 1 ){
                    loadMoreMovie()
                }
            }
        }
    }
}
