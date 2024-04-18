package com.example.themovieapp.presentation.navigation

sealed class Screen(val route: String){
    object HomeScreen:Screen("home_screen")
    object FavouriteScreen:Screen("favourite_screen")
    object SearchScreen:Screen("search_screen")
    object CategoryScreen:Screen("category_screen")
    object DetailScreen:Screen("detail_screen")
    object CastAndCrewScreen:Screen("cast_and_crew_screen")
    object ReviewScreen:Screen("review_screen")
    object BackdropScreen:Screen("backdrop_screen")
    object PosterScreen:Screen("poster_screen")
}