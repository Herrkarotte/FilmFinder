@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.filmfinder.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.filmfinder.data.MovieItem
import com.example.filmfinder.db.MovieDatabase
import com.example.filmfinder.factory.FavoritesViewModelFactory
import com.example.filmfinder.navigation.Screen
import com.example.filmfinder.viewmodel.FavoritesViewModel

@Composable
fun FavoritesScreen(
    onBack: () -> Unit, navController: NavController
) {
    val context = LocalContext.current
    val dao = MovieDatabase.getInstance(context).movieDao
    val viewModel: FavoritesViewModel = viewModel(factory = FavoritesViewModelFactory(dao, context))
    val movieState = viewModel.favorMovies
    val errorState = viewModel.error
    val previewPostersState = viewModel.previewPosterBiteArray
    LaunchedEffect(Unit) { viewModel.loadFavors() }
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "Избранное"
                )
            }, navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Назад"
                    )
                }
            })
        }) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            when {
                errorState.value != null -> ErrorMessage(error = errorState.value!!)
                movieState.value != null -> {
                    if (movieState.value?.isEmpty() == true) {
                        Text("Список пуст")
                    } else {
                        MovieList(
                            movies = movieState.value!!,
                            navController,
                            previewPostersState.value
                        )
                    }
                }

                else -> CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun MovieList(
    movies: List<MovieItem.Movie>,
    navController: NavController,
    previewPoster: Map<Int, ByteArray?>
) {
    LazyColumn {
        items(movies) { movie ->
            MoviesItem(movie, navController, previewPoster = previewPoster[movie.id])
        }
    }
}

@Composable
fun MoviesItem(movie: MovieItem.Movie, navController: NavController, previewPoster: ByteArray?) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .shadow(10.dp, shape = RectangleShape)
            .clickable {
                navController.navigate(Screen.FavDetailsScreen.createRout(movie.id))
            }) {
        Row(horizontalArrangement = Arrangement.Start) {
            if (previewPoster != null) {
                AsyncImage(
                    model = previewPoster,
                    contentDescription = movie.name,
                    modifier = Modifier.size(150.dp)
                )
            }
            Column {
                Text(
                    movie.name ?: movie.nameOriginal ?: ""
                )
                Text(
                    "Год создания ${movie.year.toString()}"
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

