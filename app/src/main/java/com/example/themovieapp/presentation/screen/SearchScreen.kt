package com.example.themovieapp.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.themovieapp.R
import com.example.themovieapp.data.source.remote.MoviesApi
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.presentation.components.BottomTab
import com.example.themovieapp.presentation.components.Header
import com.example.themovieapp.presentation.components.loadingitems.LoadingRowCard
import com.example.themovieapp.presentation.components.RatingBar
import com.example.themovieapp.presentation.navigation.Screen
import com.example.themovieapp.presentation.viewmodel.MovieDetailsViewModel
import com.example.themovieapp.presentation.viewmodel.SearchMovieViewModel
import com.example.themovieapp.utils.TabPage
import com.example.themovieapp.utils.toDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    searchViewModel: SearchMovieViewModel = hiltViewModel(),
    movieViewModel: MovieDetailsViewModel = hiltViewModel()
) {

    val tabPage by remember {
        mutableStateOf(TabPage.SEARCH)
    }
    val searchResult by searchViewModel.searchResults.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val searchQuery = remember {
        searchViewModel.searchQuery
    }
    Header(title = R.string.search, navController = navController, tabPage = tabPage)
    { paddingValues ->
        val isEmpty = searchResult.isEmpty()
        SearchBar(
            modifier = Modifier
                .padding(paddingValues),
            query = searchQuery,
            onQueryChange = {
                searchViewModel.onSearchQueryChange(it)
            },
            onSearch = { keyboardController?.hide() },
            active = true,
            onActiveChange = {},
            placeholder = { Text(text = stringResource(R.string.search_for_movies)) },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            }
        ) {
            if (isEmpty) {
                MovieListEmptyState()
            } else {

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {

                    items(
                        count = searchResult.size,
                        key = { index -> searchResult[index].id },
                        itemContent = { index ->
                            val movie = searchResult[index]
                            MovieListItem(
                                movie = movie,
                                onClickFavourite = { movieViewModel.addMovieToFavourite(movie.id) },
                                onClickMovieCard = { navController.navigate("${Screen.DetailScreen.route}/${movie.id}/${tabPage.name}") }
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MovieListEmptyState(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.no_movies_found),
            style = MaterialTheme.typography.titleSmall
        )
        Text(
            text = stringResource(R.string.try_adjusting_your_search),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun MovieListItem(
    movie: Movie,
    onClickMovieCard: () -> Unit,
    onClickFavourite: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rating = movie.voteAverage
    val isFavourite = movie.isFavourite


    Card(
        modifier = modifier
            .fillMaxSize()
            .height(150.dp)
            .padding(2.dp)
            .clickable { onClickMovieCard() },
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background.copy(alpha = 0.5f))
    ) {
        Row() {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(MoviesApi.IMAGE_BASE_URL.plus(movie.posterPath))
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = modifier.fillMaxHeight(),
            )
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = modifier
                    .padding(8.dp)
                    .fillMaxSize()
            ) {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = movie.genreNames.toString().replace("[", "").replace("]", "")
                            .replace(", ", " • "),
                        style = MaterialTheme.typography.titleSmall,
                        fontStyle = FontStyle.Italic,
                        modifier = modifier
                            .alpha(0.6f)
                    )
                    Icon(
                        imageVector = if (isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "",
                        modifier = modifier
                            .clickable { onClickFavourite() })
                }
                Text(
                    text = movie.title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,

                    )

                Text(
                    text = toDate(movie.releaseDate),
                    style = MaterialTheme.typography.titleSmall,
                    fontStyle = FontStyle.Italic,
                    modifier = modifier
                        .alpha(0.6f)
                )
                Row(
                    modifier = modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.rating_score).format(rating),
                        style = MaterialTheme.typography.titleSmall
                    )
                    if (rating != null) {
                        RatingBar(rating)
                    }

                }
            }

        }
    }
}