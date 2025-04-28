@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.filmfinder.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.filmfinder.data.MovieItem
import com.example.filmfinder.db.MovieDatabase
import com.example.filmfinder.factory.FavDetailsViewModelFactory
import com.example.filmfinder.viewmodel.FavDetailsViewModel

@Composable
fun FavDetailsScreen(
    movieId: Int, onBack: () -> Unit
) {
    val context = LocalContext.current
    val dao = MovieDatabase.getInstance(context).movieDao
    val viewModel: FavDetailsViewModel = viewModel(factory = FavDetailsViewModelFactory(dao,context))
    val filmState = viewModel.movie
    val errorState = viewModel.error

    LaunchedEffect(movieId) { viewModel.loadMovie(movieId) }

    when {
        errorState.value != null -> ErrorMessage(error = errorState.value!!)
        filmState.value != null -> FavFilmDetail(
            film = filmState.value!!,
            onBack = onBack,
            deleteFromFavor = { viewModel.deleteFromFavor() },
        )
    }
}

@Composable
fun FavFilmDetail(film: MovieItem, onBack: () -> Unit, deleteFromFavor: () -> Unit) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .windowInsetsPadding(WindowInsets.statusBars), topBar = {
            TopAppBar(
                title = {
                Text(
                    text = film.name ?: film.nameOriginal ?: ""
                )
            }, navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Назад"
                    )
                }
            }, actions = {
                IconButton(onClick = deleteFromFavor) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Удалить из избранного"
                    )
                }
            }, scrollBehavior = scrollBehavior
            )
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .windowInsetsPadding(WindowInsets.systemBars)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Описание: ${film.description ?: "-"}", fontSize = 17.sp
            )
            Text(
                text = "Жанр: ${film.genres.joinToString(", ") { it.genre ?: "" }}",
                fontSize = 17.sp
            )
            Text(
                text = "Страна производства: ${film.countries.joinToString(", ") { it.country ?: "" }}",
                fontSize = 17.sp
            )
        }
    }
}


@Composable
private fun ErrorMessage(error: String) {
    Text(text = "Ошибка $error")
}