package com.example.filmfinder.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.filmfinder.screen.DetailsScreen
import com.example.filmfinder.screen.FavDetailsScreen
import com.example.filmfinder.screen.FavoritesScreen
import com.example.filmfinder.screen.MainScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController, startDestination = Screen.MainScreen.route
    ) {
        composable(Screen.MainScreen.route) {
            MainScreen(navController = navController)
        }
        composable(
            route = Screen.DetailsScreen.route,
            arguments = listOf(navArgument("id") {
                type =
                    NavType.IntType
            })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            DetailsScreen(id = id, onBack = {
                navController.popBackStack()
            })
        }
        composable(Screen.FavoritesScreen.route) { BackStackEntry ->
            FavoritesScreen(onBack = {
                navController.popBackStack()
            },navController)
        }
        composable(
            Screen.FavDetailsScreen.route,
            arguments = listOf(navArgument("movieId") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
            FavDetailsScreen(movieId = movieId, onBack = { navController.popBackStack() })
        }
    }
}