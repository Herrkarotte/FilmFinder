package com.example.filmfinder.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import com.example.filmfinder.data.MovieData
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
                    FilmsList()
                }
            }
        }
    }
}

@Composable
fun FilmsList() {
    var film by remember { mutableStateOf<MovieData?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        isLoading = true
                        error = null
                        val response = RetrofitInterface.api.getFilm(
                            id = 301,
                            apiKey = "e30ffed0-76ab-4dd6-b41f-4c9da2b2735b"
                        )
                        withContext(Dispatchers.Main) {
                            film = response
                            isLoading = false
                        }

                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            error = e.message ?: "Произошла ошибка"
                            isLoading = false
                        }
                    }
                }
            },
            enabled = !isLoading
        ) {
            Text("Get!")
        }
        when {
            isLoading -> CircularProgressIndicator()
            error != null -> Text(
                text = "Ошибка $error"
            )

            film != null -> FilmInfo(film = film!!)
        }
    }
}

@Composable
fun FilmInfo(film: MovieData) {
    Column(
        modifier = Modifier.padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = film.name ?: "Название отсутствует"
        )
        Text(
            text =film.ratingGoodReview.toString()
        )
    }
}






