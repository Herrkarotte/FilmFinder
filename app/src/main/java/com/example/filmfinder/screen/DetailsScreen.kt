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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.filmfinder.data.Movie
import com.example.filmfinder.viewmodel.DetailsViewModel
import com.example.filmfinder.viewmodel.DetailsViewModelFactory

@Composable
fun DetailsScreen(
    id: Int, onBack: () -> Unit
) {
    val viewModel: DetailsViewModel = viewModel(factory = DetailsViewModelFactory())
    val filmState = viewModel.movie
    val isLoadingState = viewModel.isLoading
    val errorState = viewModel.error
    LaunchedEffect(id) { viewModel.loadMovie(id) }
    when {
        isLoadingState.value -> CircularProgressIndicator()
        errorState.value != null -> ErrorMessage(
            error = errorState.value!!
        )

        filmState.value != null -> FilmDetail(film = filmState.value!!, onBack)
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