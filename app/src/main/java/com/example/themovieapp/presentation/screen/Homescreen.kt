package com.example.themovieapp.presentation.screen

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.themovieapp.R
import com.example.themovieapp.data.source.remote.MoviesApi
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.presentation.viewModel.HomeViewModel
import com.example.themovieapp.utils.Category
import com.example.themovieapp.utils.toDate
import java.util.Locale

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val uiList = viewModel.homeUiState.value.movieList
    MovieLazyRow(title = R.string.top_rated,movieList = uiList)

}
@Composable
fun MovieLazyRow(
    @StringRes title: Int,
    categoryMoreMovies : () -> Unit = {}, movieList : List<Movie>, modifier: Modifier = Modifier){
    Column(modifier=modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            Text(
                text = stringResource(title),
                style = MaterialTheme.typography.headlineMedium
            )
            IconButton(modifier = Modifier, onClick = {categoryMoreMovies() }) {
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
        Text(text = title, style = MaterialTheme.typography.titleLarge)
        Text(text = toDate(date))

    }
}