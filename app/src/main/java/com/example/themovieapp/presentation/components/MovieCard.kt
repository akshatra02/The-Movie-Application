package com.example.themovieapp.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.themovieapp.R
import com.example.themovieapp.data.source.remote.MoviesApi
import com.example.themovieapp.utils.toDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardImage(
    title: String,
    date: String,
    photo: String,
    modifier: Modifier = Modifier,
    moreMovieDetails: () -> Unit
) {
    Card(
        modifier = modifier
            .width(250.dp)
            .height(400.dp)
            .padding(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        onClick = moreMovieDetails,
    ) {

        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(MoviesApi.IMAGE_BASE_URL.plus(photo))
                .crossfade(true)
                .build(),
            error = painterResource(id = R.drawable.ic_broken_image),
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