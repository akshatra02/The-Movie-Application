package com.example.themovieapp.presentation.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.themovieapp.R
import com.example.themovieapp.data.source.remote.MoviesApi
import com.example.themovieapp.domain.model.Review
import com.example.themovieapp.utils.IMAGE_BASE_URL
import com.example.themovieapp.utils.toDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewCard(
    review: Review,
    modifier: Modifier = Modifier,
    overflow: TextOverflow
) {
    val avatar = review.avatarPath ?: " "

    Card(
        modifier = modifier
            .width(400.dp)
            .padding(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {


        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(10.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(IMAGE_BASE_URL.plus(avatar))
                        .crossfade(true)
                        .build(),
                    error = painterResource(id = R.drawable.person),
                    contentScale = ContentScale.Crop,
                    contentDescription = "",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )
                Column (
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.Start,

                ){
                    Text(
                        text = "A review by ${review.author}",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(5.dp)
                    ) {
                        if (review.rating != 0.0) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .background(
                                        MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 2.dp)


                            ) {
                                Icon(imageVector = Icons.Default.Star, contentDescription = "")
                                Text(
                                    text = "${review.rating.times(10).toInt()}%".toString(),
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(4.dp)
                                )
                            }
                        }
                        Text(
                            text = toDate(review.createdAt),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }
                }
            }
            Text(
                text = review.content,
                overflow = overflow,
                modifier = Modifier.padding(4.dp)
            )
        }

    }
}