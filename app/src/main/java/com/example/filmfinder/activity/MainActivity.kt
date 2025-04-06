package com.example.filmfinder.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.filmfinder.data.MovieData
import com.example.filmfinder.ui.theme.FilmFinderTheme

class MainActivity : ComponentActivity() {
    private val movieList = listOf(
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FilmFinderTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MovieList(movieList)
                }
            }
        }
    }
}

@Composable
fun MovieList(movies: List<MovieData>) {
    LazyColumn(modifier = Modifier.navigationBarsPadding()) {
        items(movies) { movie ->
            MovieItem(movie = movie)
        }
    }
}

@Composable
fun MovieItem(movie: MovieData) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column {
            Text(
                text = movie.name
            )
            Text(
                text = movie.info
            )
            Text(
                text = movie.rating.toString()
            )
        }
    }
}