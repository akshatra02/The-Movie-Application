package com.example.themovieapp.presentation.screen

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.themovieapp.R
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.presentation.components.Header
import com.example.themovieapp.presentation.components.cards.MovieCard
import com.example.themovieapp.presentation.components.loadingitems.LoadingMovieCard
import com.example.themovieapp.presentation.navigation.Screen
import com.example.themovieapp.presentation.viewmodel.HomeViewModel
import com.example.themovieapp.utils.Category
import com.example.themovieapp.utils.IMAGE_BASE_URL
import com.example.themovieapp.utils.TabPage


@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    val tabPage by remember {
        mutableStateOf(TabPage.HOME)
    }
    val categories = listOf(
        Triple(
            R.string.now_playing, homeUiState.nowPlayingUiState, Category.NOW_PLAYING
        ), Triple(
            R.string.upcoming, homeUiState.upcomingUiState, Category.UPCOMING
        ), Triple(
            R.string.top_rated, homeUiState.topRatedUiState, Category.TOP_RATED
        ), Triple(R.string.popular, homeUiState.popularUiState, Category.POPULAR)
    )
    Header(title = R.string.movies, navController = navController, tabPage = tabPage)
    { paddingValue ->
        Column(
            modifier = Modifier.padding(paddingValue)
        ) {
            if (homeUiState.nowPlayingUiState.isLoading || homeUiState.nowPlayingUiState.movieList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {

                LazyColumn {
                    item {
                        homeUiState.nowPlayingUiState.movieList.first()?.let {
                            BackgroundWithTextAndButton(
                                background = it.posterPath,
                            )
                        }
                    }
                    items(items = categories, key = { it.third }) { item ->
                        val titleResId = item.first
                        val movieListUi = item.second
                        val category = item.third
                        MovieLazyRow(
                            title = titleResId,
                            movieList = movieListUi.movieList,
                            tabPage = tabPage,
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

@Composable
fun BackgroundWithTextAndButton(
    background: String,
) {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomStart
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(IMAGE_BASE_URL.plus(background)).crossfade(true).build(),
            error = painterResource(id = R.drawable.ic_broken_image),
            contentScale = ContentScale.Crop,
            alpha = .5f,
            contentDescription = "hello",
            modifier = Modifier
                .fillMaxSize()
                .height(400.dp)
                .clip(RoundedCornerShape(16.dp))
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            Text(
                text = stringResource(R.string.welcome_message),
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun MovieLazyRow(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
    tabPage: TabPage,
    categoryMoreMovies: () -> Unit = {},
    isLoading: Boolean,
    loadMoreMovie: () -> Unit = {},
    movieList: List<Movie?>,
    navController: NavController
) {
    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            Text(
                text = stringResource(title),
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                modifier = modifier.padding(start = 5.dp)
            )
            Text(
                text = "See All ",
                style = MaterialTheme.typography.titleMedium,
                modifier = modifier
                    .padding(start = 5.dp)
                    .clickable {
                        categoryMoreMovies()
                    }
            )
        }
        if (movieList.isEmpty()) {
            LazyRow {
                items(3) {
                    LoadingMovieCard()
                }
            }
        } else {
            LazyRow(modifier = modifier.padding(if (isLoading) 16.dp else 0.dp)) {
                items(movieList.size) { i ->
                    val currentUiState = movieList.get(i)
                    if (currentUiState != null) {
                        MovieCard(
                            title = currentUiState.title,
                            date = currentUiState.releaseDate,
                            rating = currentUiState.voteAverage,
                            photo = currentUiState.posterPath,
                            moreMovieDetails = {
                                navController.navigate("${Screen.DetailScreen.route}/${currentUiState.id}/${tabPage.name}")
                            },
                            modifier = modifier
                        )
                        if (i >= movieList.size - 1 && !isLoading) {
                            LoadingMovieCard()
                            LaunchedEffect(key1 = movieList) {
                                loadMoreMovie()
                            }
                        }
                    }
                }
            }
        }
    }
}
