@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.filmfinder.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.filmfinder.data.MovieItem
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
fun FilmDetail(film: MovieItem, onBack: () -> Unit) {
    val scrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = film.name ?: "Без названия"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { TODO() }) {
                        Icon(
                            imageVector = Icons.Filled.FavoriteBorder,
                            contentDescription = "Добавить в избранное"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            AsyncImage(
                model = film.posterUrl,
                contentDescription = film.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(700.dp)
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

        }
    }
}

@Composable
private fun ErrorMessage(error: String) {
    Text(text = "Ошибка $error")
}