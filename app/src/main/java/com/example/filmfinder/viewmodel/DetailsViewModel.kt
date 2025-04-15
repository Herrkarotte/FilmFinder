package com.example.filmfinder.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmfinder.data.MovieItem
import kotlinx.coroutines.launch
import model.FilmModel

class DetailsViewModel(private val model: FilmModel) : ViewModel() {
    private val _movie = mutableStateOf<MovieItem?>(null)
    val movie: State<MovieItem?> = _movie

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    fun loadMovie(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                _movie.value = model.getFilm(id = id)
            } catch (e: Exception) {
                _error.value = e.message ?: "Loading error"
            } finally {
                _isLoading.value = false
            }
        }
    }
}