package com.example.filmfinder.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.filmfinder.data.Movie
import com.example.filmfinder.viewmodel.MainViewModel
import com.example.filmfinder.viewmodel.MainViewModelFactory
import navigation.Screen

@Composable
fun MainScreen(navController: NavController) {
    val viewModel: MainViewModel = viewModel(factory = MainViewModelFactory())
    val filmsState = viewModel.films
    val isLoadingState = viewModel.isLoading
    val errorState = viewModel.error

    LaunchedEffect(Unit) { viewModel.loadFilms() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        when {
            isLoadingState.value -> CircularProgressIndicator()
            errorState.value != null -> ErrorMessage(error = errorState.value!!)
            filmsState.value != null -> {
                if (filmsState.value == null) {
                    Text("Список пуст")
                } else {
                    FilmsList(films = filmsState.value!!, navController)
                }
            }
        }

    }
}

@Composable
fun FilmsList(films: List<Movie>, navController: NavController) {
    LazyColumn(modifier = Modifier.navigationBarsPadding()) {
        items(films) { film -> FilmsItem(film, navController) }
    }
}

@Composable
fun FilmsItem(film: Movie, navController: NavController) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .shadow(10.dp, shape = RectangleShape)
            .clickable {
                navController.navigate(Screen.DetailsScreen.createRout(film.id))
            }) {
        Row(horizontalArrangement = Arrangement.Start) {
            AsyncImage(
                model = film.posterUrlPreview, contentDescription = film.name,
                modifier = Modifier.size(150.dp)
            )
            Column {
                Text(
                    film.name ?: "Имя отсутствует"
                )
                Text(
                    "Год создания ${film.year.toString()}"
                )
            }
        }
    }
}


@Composable
private fun ErrorMessage(error: String) {
    Text(
        text = "Ошибка $error"
    )
}



