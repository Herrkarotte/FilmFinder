@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.filmfinder.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.filmfinder.data.MovieItem
import com.example.filmfinder.db.MovieDatabase
import com.example.filmfinder.factory.MainViewModelFactory
import com.example.filmfinder.navigation.Screen
import com.example.filmfinder.viewmodel.MainViewModel

@Composable
fun MainScreen(navController: NavController) {
    val context = LocalContext.current
    val dao = MovieDatabase.getInstance(context).movieDao
    val viewModel: MainViewModel = viewModel(factory = MainViewModelFactory(dao))
    val films = viewModel.filmsPagingFlow.collectAsLazyPagingItems()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarMessage = viewModel.snackBarMessage.value

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message, withDismissAction = true, duration = SnackbarDuration.Short
            )
            viewModel.clearSnackbar()
        }
    }


    Scaffold(modifier = Modifier.clickable {
        focusManager.clearFocus()
    }, snackbarHost = { SnackbarHost(snackbarHostState) }, topBar = {
        Search(
            query = searchQuery,
            onChange = viewModel::onSearchQueryChanged,
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.statusBars)
                .fillMaxWidth()
        )
    }, bottomBar = {
        BottomAppBar(
            modifier = Modifier.height(100.dp), actions = {
                IconButton(onClick = {
                    navController.navigate(Screen.FavoritesScreen.route)
                }) {
                    Icon(
                        Icons.Filled.FavoriteBorder, contentDescription = "Избранное"
                    )
                }
            })
    }) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
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
                    FilmsList(
                        films = films,
                        navController,
                        addToFavor = { id -> viewModel.toggleFavor(id) })
                }
            }
        }
    }
}


@Composable
fun FilmsList(
    films: LazyPagingItems<MovieItem>, navController: NavController, addToFavor: (Int) -> Unit
) {
    LazyColumn {
        items(films.itemCount) { index ->
            films[index]?.let { film ->
                FilmsItem(film, navController, addToFavor = { id -> addToFavor(id) })
            }
        }
    }
}

@Composable
fun FilmsItem(
    film: MovieItem,
    navController: NavController,
    addToFavor: (Int) -> Unit,

    ) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .shadow(10.dp, shape = RectangleShape)
            .combinedClickable(
                onClick = { navController.navigate(Screen.DetailsScreen.createRout(film.id)) },
                onLongClick = {
                    addToFavor(film.id)
                })
    ) {
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



