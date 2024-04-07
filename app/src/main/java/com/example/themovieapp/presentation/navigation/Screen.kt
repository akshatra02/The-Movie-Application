package com.example.themovieapp.presentation.navigation

sealed class Screen(val route: String){
    object HomeScreen:Screen("home_screen")
    object CategoryScreen:Screen("category_screen")
    object DetailScreen:Screen("detail_screen")
}