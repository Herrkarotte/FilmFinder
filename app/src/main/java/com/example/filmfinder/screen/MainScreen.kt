package com.example.filmfinder.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.filmfinder.data.MovieData
import com.example.filmfinder.navigation.Screen

@Composable
fun MainScreen(navController: NavController) {
    val movieList = listOf(
        MovieData(1, "Фильм 1", "Информация", 8.1),
        MovieData(2, "Фильм 2", "Информация", 7.1),
        MovieData(3, "Фильм 3", "Информация", 6.2),
        MovieData(4, "Фильм 4", "Информация", 8.3),
        MovieData(5, "Фильм 5", "Информация", 4.5),
        MovieData(6, "Фильм 6", "Информация", 6.0),
        MovieData(7, "Фильм 7", "Информация", 9.5),
        MovieData(8, "Фильм 8", "Информация", 8.1),
        MovieData(9, "Фильм 9", "Информация", 6.7),
        MovieData(10, "Фильм 10", "Информация", 9.1),
        MovieData(11, "Фильм 11", "Информация", 10.0),
        MovieData(12, "Фильм 12", "Информация", 8.0),
        MovieData(13, "Фильм 13", "Информация", 7.0),
        MovieData(14, "Фильм 14", "Информация", 6.0),
        MovieData(15, "Фильм 15", "Информация", 5.0)
    )

    LazyColumn(modifier = Modifier
        .navigationBarsPadding()
        .statusBarsPadding()) {
        items(movieList) { movie ->
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(Screen.DetailScreen.createRout(movie.id))
                    }
            ) {
                Column {
                    Text(
                        text = movie.name
                    )
                }
            }
        }
    }
}





