@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.filmfinder.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.example.filmfinder.data.Movie
import com.example.filmfinder.viewmodel.MainViewModel
import com.example.filmfinder.viewmodel.MainViewModelFactory
import navigation.Screen

@Composable
fun MainScreen(navController: NavController) {
    val viewModel: MainViewModel = viewModel(factory = MainViewModelFactory())
    val films = viewModel.filmsPagingFlow.collectAsLazyPagingItems()

    Scaffold(
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.height(100.dp),
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.FavoritesScreen.route)
                    }) {
                        Icon(
                            Icons.Filled.FavoriteBorder,
                            contentDescription = "Избранное"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                films.loadState.refresh is LoadState.Loading -> {
                    CircularProgressIndicator()
                }

                films.loadState.refresh is LoadState.Error -> {
                    ErrorMessage(
                        error = (films.loadState.refresh as LoadState.Error).error.message
                            ?: "Неизвестная ошибка"
                    )
                }

                else -> {
                    FilmsList(films = films, navController)
                }
            }
        }
    }
}


@Composable
fun FilmsList(films: LazyPagingItems<Movie>, navController: NavController) {
    LazyColumn {
        items(films.itemCount) { index ->
            films[index]?.let { film ->
                FilmsItem(film, navController)
            }
        }
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



