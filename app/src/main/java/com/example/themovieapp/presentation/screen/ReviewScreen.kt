package com.example.themovieapp.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.themovieapp.domain.model.Review
import com.example.themovieapp.presentation.components.Header
import com.example.themovieapp.presentation.components.cards.ReviewCard
import com.example.themovieapp.presentation.components.loadingitems.LoadingRowCard
import com.example.themovieapp.presentation.navigation.Screen
import com.example.themovieapp.presentation.viewmodel.MovieDetailsViewModel
import com.example.themovieapp.utils.TabPage


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(
    navController: NavController,
    viewModel: MovieDetailsViewModel = hiltViewModel()
) {

    val tabPage by remember {
        mutableStateOf(TabPage.HOME)
    }
    val reviewListUiState by viewModel.reviewListUiState.collectAsState()
    val movieAndExtraDetailUiState by viewModel.movieAndExtraDetailUiState.collectAsState()
    val reviewList = reviewListUiState.review
    val title = movieAndExtraDetailUiState.movieAndExtraDetails?.title ?: ""
    val movieId = movieAndExtraDetailUiState.movieAndExtraDetails?.id ?: -1
    Header(
        title = title, navController = navController,
        navigateOnClick = {

            navController.navigate("${Screen.DetailScreen.route}/${movieId}/${tabPage.name}") {
                popUpTo("${Screen.DetailScreen.route}/${movieId}/${tabPage.name}") {
                    inclusive = true
                }
            }
        },
        tabPage = tabPage, showIcon = false, showBackButton = true,
    )
    { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            if (reviewListUiState.isLoading) {

                LazyColumn {
                    items(5) {
                        LoadingRowCard()
                    }
                }
            } else {
                ReviewScreenContent(reviewList = reviewList)
            }
        }
    }
}

@Composable
fun ReviewScreenContent(reviewList: List<Review>, reviewForMovie: () -> Unit = {}) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Reviews ",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(start = 5.dp),
            )
        }

    }
    LazyColumn {
        items(reviewList.size) { index ->
            val review = reviewList[index]
            ReviewCard(
                review = review,
                modifier = Modifier
                    .height(IntrinsicSize.Max),
                overflow = TextOverflow.Visible

            )
        }
    }
}