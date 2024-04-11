package com.example.themovieapp.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.themovieapp.data.source.remote.MoviesApi
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.presentation.components.BottomTab
import com.example.themovieapp.presentation.navigation.Screen
import com.example.themovieapp.presentation.viewmodel.SearchMovieViewModel
import com.example.themovieapp.utils.TabPage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController:NavController,
    viewModel: SearchMovieViewModel = hiltViewModel()
) {
    var tabPage by remember {
        mutableStateOf(TabPage.HOME)
    }
    val searchResult by viewModel.searchResults.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current
    val searchQuery = viewModel.searchQuery
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Search") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(Screen.HomeScreen.route) {
                            popUpTo(Screen.HomeScreen.route) {
                                inclusive = true
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                BottomTab(navController = navController, tabPage = tabPage, onTabSelected = { tabPage = it})
            }

        }

    ) { paddingValues ->

        SearchBar(
            modifier = Modifier
                .padding(paddingValues),
            query = searchQuery,
            onQueryChange = {
                viewModel.onSearchQueryChange(it)
            },
            onSearch = { keyboardController?.hide() },
            active = true,
            onActiveChange = {},
            placeholder = { Text(text = "Search for movies...") },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            }
        ) {
                if (searchResult.isEmpty()) {
                    MovieListEmptyState()
                } else {

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(32.dp),
                        contentPadding = PaddingValues(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {

                        items(
                            count = searchResult.size,
                            key = { index -> searchResult[index].id },
                            itemContent = { index ->
                                val movie = searchResult[index]
                                MovieListItem(movie = movie, navController = navController)
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
            text = "No movies found",
            style = MaterialTheme.typography.titleSmall
        )
        Text(
            text = "Try adjusting your search",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
@Composable
fun MovieListItem(
    movie: Movie,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(MoviesApi.IMAGE_BASE_URL.plus(movie.posterPath))
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = modifier
                .size(50.dp)
                .aspectRatio(.8f)
        )
        Text(
            text = movie.title,
            modifier = modifier
                .clickable { navController.navigate("${Screen.DetailScreen.route}/${movie.id}") }
                .align(Alignment.CenterVertically)
        )

    }
}