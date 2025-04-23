package com.example.filmfinder.navigation

sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")

    object DetailsScreen : Screen("details_screen/{id}") {
        fun createRout(id: Int) = "details_screen/$id"
    }

    object FavoritesScreen : Screen("favorites_screen")

    object FavDetailsScreen : Screen("favdetails_screen/{movieId}") {
        fun createRout(movieId: Int) = "favdetails_screen/$movieId"
    }
}