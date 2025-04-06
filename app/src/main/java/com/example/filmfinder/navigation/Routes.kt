package com.example.filmfinder.navigation

sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")
    object DetailScreen : Screen("details_screen/{id}") {
        fun createRout(id: Int) = "details_screen/$id"
    }
}