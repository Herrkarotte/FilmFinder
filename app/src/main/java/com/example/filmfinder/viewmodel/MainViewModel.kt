package com.example.filmfinder.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmfinder.data.Movie
import kotlinx.coroutines.launch
import model.FilmModel

class MainViewModel(private val model: FilmModel) : ViewModel() {
    private val _films = mutableStateOf<List<Movie>?>(null)
    val films: State<List<Movie>?> = _films

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    fun loadFilms() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _films.value = model.getFilmList().movies
            } catch (e: Exception) {
                _error.value = e.message ?: "Loading error"
            } finally {
                _isLoading.value = false
            }
        }
    }
}