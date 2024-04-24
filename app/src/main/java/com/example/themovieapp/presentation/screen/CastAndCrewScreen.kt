package com.example.themovieapp.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.themovieapp.domain.model.CastAndCrew
import com.example.themovieapp.presentation.components.Header
import com.example.themovieapp.presentation.components.cards.CastAndCrewCard
import com.example.themovieapp.presentation.components.loadingitems.LoadingRowCard
import com.example.themovieapp.presentation.navigation.Screen
import com.example.themovieapp.presentation.viewmodel.MovieDetailsViewModel
import com.example.themovieapp.utils.TabPage

@Composable
fun CastAndCrewScreen(
    navController: NavController,
    viewModel: MovieDetailsViewModel = hiltViewModel()
) {
    val tabPage by remember {
        mutableStateOf(TabPage.HOME)
    }
    val uiState by viewModel.uiState.collectAsState()
    val castAndCrewList = uiState.castAndCrewState
    val title = uiState.movieAndExtraDetails?.title ?: ""
    val movieId = uiState.movieAndExtraDetails?.id ?: -1
    Header(
        title = title,
        navController = navController,
        tabPage = tabPage,
        showIcon = false,
        showBackButton = true,
        navigateOnClick = {

            navController.navigate("${Screen.DetailScreen.route}/${movieId}/${tabPage.name}")
            {
                popUpTo("${Screen.DetailScreen.route}/${movieId}/${tabPage.name}") {
                    inclusive = true
                }
            }
        })
    { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            if (castAndCrewList.isLoading) {
                LazyColumn {
                    items(5) {
                        LoadingRowCard()
                    }
                }
            } else {
                CastAndCrewContent(castAndCrewList = castAndCrewList.castAndCrew)
            }
        }
    }
}

@Composable
private fun CastAndCrewContent(modifier: Modifier = Modifier, castAndCrewList: List<CastAndCrew>?) {
    if (castAndCrewList == null) return
    val castList = castAndCrewList.filter { it.isCast }.sortedBy { it.order }
    val crewList = castAndCrewList.filter { !it.isCast }
    Column {
        LazyColumn {
            if (castList.isNotEmpty()) {
                item {
                    Text(
                        text = "Cast ${castList.size}",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
                items(castList.size, key = { castList[it].personId }) { index ->
                    val cast = castList[index]
                    CastAndCrewCard(
                        title = cast.name,
                        role = cast.role,
                        photo = cast.profilePath
                    )
                }
            }
            if (crewList.isNotEmpty()) {
                item {
                    Divider(modifier = modifier.padding(8.dp))
                    Text(
                        text = "Crew ${crewList.size}",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = modifier.padding(vertical = 16.dp)
                    )
                }
                val departmentList = crewList.map { it.knowForDepartment }.toSortedSet()
                departmentList.forEach { departmentName ->
                    val dept = crewList.filter { it.knowForDepartment == departmentName }
                    item {
                        Text(
                            text = departmentName,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                    items(dept.size) { index ->
                        val crew = dept[index]
                        CastAndCrewCard(
                            title = crew.name,
                            role = crew.role,
                            photo = crew.profilePath
                        )
                    }
                }
            }

        }
    }
}