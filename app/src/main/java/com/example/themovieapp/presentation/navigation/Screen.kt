package com.example.themovieapp.presentation.navigation

sealed class Screen(val route: String){
    object HomeScreen:Screen("home_screen")
    object FavouriteScreen:Screen("favourite_screen")
    object SearchScreen:Screen("search_screen")
    object CategoryScreen:Screen("category_screen")
    object DetailScreen:Screen("detail_screen")
}