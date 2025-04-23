package com.example.filmfinder.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmfinder.data.MovieItem
import com.example.filmfinder.model.FavModel
import kotlinx.coroutines.launch

class FavDetailsViewModel(private val model: FavModel) : ViewModel() {
    private val _movie = mutableStateOf<MovieItem?>(null)
    val movie: State<MovieItem?> = _movie

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    fun loadMovie(movieId: Int) {
        viewModelScope.launch {
            _error.value = null
            try {
                _movie.value = model.getFavoriteMovie(movieId)
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