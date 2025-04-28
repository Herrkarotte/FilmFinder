package com.example.filmfinder.viewmodel

import android.graphics.BitmapFactory
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmfinder.data.MovieItem
import com.example.filmfinder.model.FavModel
import kotlinx.coroutines.launch

class FavDetailsViewModel(private val model: FavModel) : ViewModel() {
    private val _movie = mutableStateOf<MovieItem?>(null)
    val movie: State<MovieItem?> = _movie

    private val _posterBitmap = mutableStateOf<ImageBitmap?>(null)
    val posterBitmap: State<ImageBitmap?> = _posterBitmap

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    fun loadMovie(movieId: Int) {
        viewModelScope.launch {
            _error.value = null
            try {
                _movie.value = model.getFavoriteMovie(movieId)
                _movie.value?.let { movie ->
                    val posterBytes = model.getMoviePosterBytes(movie.id)
                    posterBytes?.let { bytes ->
                        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        _posterBitmap.value = bitmap.asImageBitmap()
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Loading error"
            }
        }
    }

    fun deleteFromFavor() {
        viewModelScope.launch {
            _movie.value?.let { movie ->
                model.removeFromFavor(movie)
            }
        }
    }
}