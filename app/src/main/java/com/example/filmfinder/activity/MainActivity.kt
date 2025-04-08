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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.filmfinder.data.Movie
import com.example.filmfinder.retrofit.RetrofitInterface
import com.example.filmfinder.ui.theme.FilmFinderTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FilmFinderTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    FilmsListScreen()
                }
            }
        }
    }
}

@Composable
fun FilmsListScreen() {
    var films by remember { mutableStateOf<List<Movie>?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                loadFilms(
                    onLoading = { isLoading = true },
                    onSuccess = { filmList ->
                        films = filmList
                        isLoading = false
                        error = null
                    },
                    onError = { errorMessage ->
                        error = errorMessage
                        isLoading = false
                        films = null
                    }
                )
            },
            enabled = !isLoading
        ) {
            Text("Get top films!")
        }
        when {
            isLoading -> CircularProgressIndicator()
            error != null -> ErrorMessage(error = error!!)
            films != null -> {
                if (films!!.isEmpty()) {
                    Text("Список пуст")
                } else {
                    FilmsList(films = films!!)
                }
            }
        }

    }
}

fun loadFilms(
    onLoading: () -> Unit,
    onSuccess: (List<Movie>) -> Unit,
    onError: (String) -> Unit
) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            onLoading()
            val response = RetrofitInterface.api.getFilmsList(
                apiKey = "e30ffed0-76ab-4dd6-b41f-4c9da2b2735b"
            )
            val films = response.movies ?: emptyList()
            withContext(Dispatchers.Main) {
                if (films.isNotEmpty()) {
                    onSuccess(films)
                } else {
                    onError("Список пуст")
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                onError(e.message ?: "Ошибка загрузки")
            }
        }
    }
}

@Composable
fun FilmsList(films: List<Movie>) {
    LazyColumn(modifier = Modifier.navigationBarsPadding()) {
        items(films) { film -> FilmsItem(film) }
    }
}

@Composable
fun FilmsItem(film: Movie) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    )
    {
        Column {
            Text(
                film.name ?: "Имя отсутствует"
            )
            Text(
                film.originalName?:"Оригинального имени нет"
            )
        }
    }
}

@Composable
fun ErrorMessage(error: String) {
    Text(
        text = "Ошибка $error"
    )
}



