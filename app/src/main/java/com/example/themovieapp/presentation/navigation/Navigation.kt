package com.example.themovieapp.presentation.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.themovieapp.presentation.screen.BackDrop
import com.example.themovieapp.presentation.screen.BackdropScreen
import com.example.themovieapp.presentation.screen.CastAndCrewScreen
import com.example.themovieapp.presentation.screen.CategoryScreen
import com.example.themovieapp.presentation.screen.DetailScreen
import com.example.themovieapp.presentation.screen.FavouriteScreen
import com.example.themovieapp.presentation.screen.HomeScreen
import com.example.themovieapp.presentation.screen.PosterScreen
import com.example.themovieapp.presentation.screen.ReviewScreen
import com.example.themovieapp.presentation.screen.SearchScreen


@Composable
fun Navigation(
) {
    val navController: NavHostController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.HomeScreen.route) {

        composable(Screen.HomeScreen.route) {
            HomeScreen(navController)
        }
        composable(Screen.FavouriteScreen.route) {
            FavouriteScreen(navController)
        }
        composable(Screen.SearchScreen.route) {
            SearchScreen(navController)
        }
        composable(
            route = "${Screen.CategoryScreen.route}/{category}",
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            CategoryScreen(navController, category = category)
        }
        composable(
            route = "${Screen.DetailScreen.route}/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: -1

            DetailScreen(navController)
        }
        composable(
            route = "${Screen.CastAndCrewScreen.route}/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: -1

            CastAndCrewScreen(navController)
        }
        composable(
            route = "${Screen.ReviewScreen.route}/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: -1

            ReviewScreen(navController)
        }
        composable(
            route = "${Screen.BackdropScreen.route}/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: -1

            BackdropScreen(navController)
        }
        composable(
            route = "${Screen.PosterScreen.route}/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: -1

            PosterScreen(navController)
        }

    }
}