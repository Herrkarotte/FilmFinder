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
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.filmfinder.data.MovieItem
import com.example.filmfinder.db.MovieDatabase
import com.example.filmfinder.factory.DetailsViewModelFactory
import com.example.filmfinder.viewmodel.DetailsViewModel

@Composable
fun DetailsScreen(
    id: Int, onBack: () -> Unit
) {
    val context = LocalContext.current
    val dao = MovieDatabase.getInstance(context).movieDao
    val viewModel: DetailsViewModel = viewModel(factory = DetailsViewModelFactory(dao))
    val filmState = viewModel.movie
    val isLoadingState = viewModel.isLoading
    val errorState = viewModel.error
    val isFavorState = viewModel.isFavor


    LaunchedEffect(id) { viewModel.loadMovie(id) }
    when {
        isLoadingState.value -> CircularProgressIndicator()
        errorState.value != null -> ErrorMessage(
            error = errorState.value!!
        )

        filmState.value != null -> FilmDetail(
            film = filmState.value!!,
            onBack,
            addFavor = { viewModel.toggleFavor() },
            isFavorState.value
        )
    }

}

@Composable
fun FilmDetail(film: MovieItem, onBack: () -> Unit, addFavor: () -> Unit, isFavor: Boolean) {
    val scrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .windowInsetsPadding(WindowInsets.statusBars),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = film.name ?: film.nameOriginal ?: ""
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
                    IconButton(onClick = addFavor) {
                        Icon(
                            imageVector = if (isFavor) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            tint = if (isFavor) Color.Red else Color.Black,
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
                .windowInsetsPadding(WindowInsets.systemBars)
                .verticalScroll(rememberScrollState())
        ) {
//            AsyncImage(
//                model = film.posterUrl,
//                contentDescription = film.name,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(700.dp)
//            )

            Text(
                text = "Описание: ${film.description ?: " - "}",
                fontSize = 17.sp
            )
            Text(
                text = "Жанр: ${film.genres.joinToString(", ") { it.genre ?: " " }}",
                fontSize = 17.sp
            )
            Text(
                "Страна производства: ${film.countries.joinToString(", ") { it.country ?: " " }}",
                fontSize = 17.sp
            )

        }
    }
}

@Composable
private fun ErrorMessage(error: String) {
    Text(text = "Ошибка $error")
}