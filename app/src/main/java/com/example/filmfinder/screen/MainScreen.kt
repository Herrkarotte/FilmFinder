@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.filmfinder.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.example.filmfinder.data.MovieItem
import com.example.filmfinder.navigation.Screen
import com.example.filmfinder.viewmodel.MainViewModel
import com.example.filmfinder.viewmodel.MainViewModelFactory

@Composable
fun MainScreen(navController: NavController) {
    val viewModel: MainViewModel = viewModel(factory = MainViewModelFactory())
    val films = viewModel.filmsPagingFlow.collectAsLazyPagingItems()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier.clickable {
            focusManager.clearFocus()
        },
        topBar = {
            Search(
                query = searchQuery,
                onChange = viewModel::onSearchQueryChanged,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .fillMaxWidth()
            )
        },
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
fun FilmsList(films: LazyPagingItems<MovieItem>, navController: NavController) {
    LazyColumn {
        items(films.itemCount) { index ->
            films[index]?.let { film ->
                FilmsItem(film, navController)
            }
        }
    }
}

@Composable
fun FilmsItem(film: MovieItem, navController: NavController) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .shadow(10.dp, shape = RectangleShape)
            .clickable {
                navController.navigate(Screen.DetailsScreen.createRout(film.id))
            }) {
        Row(horizontalArrangement = Arrangement.Start) {
//            AsyncImage(
//                model = film.posterUrlPreview, contentDescription = film.name,
//                modifier = Modifier.size(150.dp)
//            )
            Column {
                Text(
                    film.name ?: film.nameOriginal ?: ""
                )
                Text(
                    "Год создания ${film.year.toString()}"
                )
            }
        }
    }
}

@Composable
fun Search(query: String, onChange: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = query,
        onValueChange = onChange,
        modifier = modifier,
        placeholder = { Text("Поиск по ключевому слову") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "поиск") },
        singleLine = true
    )
}

@Composable
private fun ErrorMessage(error: String) {
    Text(
        text = "Ошибка $error"
    )
}



