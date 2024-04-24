package com.example.themovieapp.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Double = 0.0
) {
    val stars = 5
    val color = MaterialTheme.colorScheme.primary
    val averageRating = rating / 2
    val filledStars = kotlin.math.floor(averageRating).toInt()
    val unFilledStars = (stars - kotlin.math.ceil(averageRating)).toInt()
    val halfStars = !(averageRating.rem(2).equals(0.0))

    Row(
        modifier = modifier
    ) {
        repeat(filledStars) {
            Icon(imageVector = Icons.Default.Star, contentDescription = "", tint = color)
        }
        if (halfStars) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.StarHalf,
                contentDescription = "",
                tint = color
            )
        }
        repeat(unFilledStars) {
            Icon(imageVector = Icons.Default.StarBorder, contentDescription = "", tint = color)
        }
    }


}