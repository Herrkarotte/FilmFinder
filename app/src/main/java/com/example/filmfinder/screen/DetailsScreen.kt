package com.example.filmfinder.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.filmfinder.data.Movie
import com.example.filmfinder.retrofit.RetrofitInterface

@Composable
fun DetailsScreen(
    id: Int, onBack: () -> Unit
) {
    var film by remember { mutableStateOf<Movie?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(id) {
        try {
            val response = RetrofitInterface.api.getFilm(
                id = id, apiKey = "e30ffed0-76ab-4dd6-b41f-4c9da2b2735b"
            )
            film = response
            isLoading = false
        } catch (e: Exception) {
            error = e.message ?: "Ошибка загрузки"
            isLoading = false
        }
    }
    when {
        isLoading -> CircularProgressIndicator()
        error != null -> ErrorMessage(
            error = error!!
        )

        film != null -> FilmDetail(film = film!!, onBack)
    }

}


@Composable
fun FilmDetail(film: Movie, onBack: () -> Unit) {
    Column(
        modifier = Modifier.statusBarsPadding()
    ) {
        AsyncImage(
            model = film.posterUrl,
            contentDescription = film.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
        Text(
            text = film.name ?: "Без названия"
        )
        Text(
            text = "Описание: ${film.description ?: "Без описания"}"
        )
        Text(
            text = "Жанр: ${film.genres.joinToString(", ") { it.genre ?: " " }}"
        )
        Text(
            "Страна производства: ${film.countries.joinToString(", ") { it.country ?: " " }}"
        )
        Button(onBack) {
            Text("Назад")
        }
    }
}

@Composable
private fun ErrorMessage(error: String) {
    Text(text = "Ошибка $error")
}