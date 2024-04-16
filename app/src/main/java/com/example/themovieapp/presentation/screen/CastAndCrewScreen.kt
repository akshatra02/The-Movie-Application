package com.example.themovieapp.presentation.screen

import android.util.Log
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.themovieapp.presentation.components.PersonCard
import com.example.themovieapp.presentation.navigation.Screen
import com.example.themovieapp.presentation.viewmodel.MovieDetailsViewModel

@Composable
fun CastAndCrewScreen(navController: NavController, viewModel: MovieDetailsViewModel = hiltViewModel()) {

    val castAndCrewListUiState = viewModel.castAndCrewListUiState.collectAsState().value
    val castAndCrewList = castAndCrewListUiState.castAndCrew
    Log.d("CrewCAst",castAndCrewList.size.toString())

    Text(text = "LOPOP")
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(castAndCrewList.size) { index ->
            val castAndCrew = castAndCrewList[index]
            PersonCard(
                title = castAndCrew.name,
                role = castAndCrew.role,
                photo = castAndCrew.profilePath
            )
        }
    }

}