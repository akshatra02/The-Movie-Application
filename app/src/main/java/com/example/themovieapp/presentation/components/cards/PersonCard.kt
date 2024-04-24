package com.example.themovieapp.presentation.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.themovieapp.R
import com.example.themovieapp.utils.IMAGE_BASE_URL


@Composable
fun PersonCard(
    title: String,
    role: String,
    photo: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .width(150.dp)
            .height(250.dp)
            .padding(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {

        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(IMAGE_BASE_URL.plus(photo))
                .crossfade(true)
                .build(),
            error = painterResource(id = R.drawable.person),
            contentScale = ContentScale.Crop,
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .size(150.dp)
        )
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title, style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = role,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = modifier.alpha(0.8f)
            )
        }

    }
}