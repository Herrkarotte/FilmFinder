package com.example.filmfinder.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.filmfinder.data.MovieData

@Composable
fun DetailsScreen(
    id: Int?,
    onBack: () -> Unit
) {
    val movie = remember(id) {
        when (id) {
            1 -> MovieData(1, "Фильм 1", "Информация", 8.1)
            2 -> MovieData(2, "Фильм 2", "Информация", 7.1)
            3 -> MovieData(3, "Фильм 3", "Информация", 6.2)
            4 -> MovieData(4, "Фильм 4", "Информация", 8.3)
            5 -> MovieData(5, "Фильм 5", "Информация", 4.5)
            6 -> MovieData(6, "Фильм 6", "Информация", 6.0)
            7 -> MovieData(7, "Фильм 7", "Информация", 9.5)
            8 -> MovieData(8, "Фильм 8", "Информация", 8.1)
            9 -> MovieData(9, "Фильм 9", "Информация", 6.7)
            10 -> MovieData(10, "Фильм 10", "Информация", 9.1)
            11 -> MovieData(11, "Фильм 11", "Информация", 10.0)
            12 -> MovieData(12, "Фильм 12", "Информация", 8.0)
            13 -> MovieData(13, "Фильм 13", "Информация", 7.0)
            14 -> MovieData(14, "Фильм 14", "Информация", 6.0)
            15 -> MovieData(15, "Фильм 15", "Информация", 5.0)
            else -> null
        }
    }
    Column(modifier = Modifier.statusBarsPadding()) {
        movie?.let {
            Text(it.info)
            Text(it.rating.toString())
        } ?: Text("Не найдено")
        Button(onClick = onBack) {
            Text("Назад")
        }
    }
}
