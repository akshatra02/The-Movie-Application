package com.example.themovieapp.presentation.navigation

sealed class Screen(val route: String) {
    data object HomeScreen : Screen("home_screen")
    data object FavouriteScreen : Screen("favourite_screen")
    data object SearchScreen : Screen("search_screen")
    data object CategoryScreen : Screen("category_screen")
    data object DetailScreen : Screen("detail_screen")
    data object CastAndCrewScreen : Screen("cast_and_crew_screen")
    data object ReviewScreen : Screen("review_screen")
    data object BackdropScreen : Screen("backdrop_screen")
    data object PosterScreen : Screen("poster_screen")
}