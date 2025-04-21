package com.example.filmfinder.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmfinder.data.MovieItem
import com.example.filmfinder.model.FavModel
import com.example.filmfinder.model.FilmModel
import kotlinx.coroutines.launch

class DetailsViewModel(private val model: FilmModel, private val favModel: FavModel) : ViewModel() {
    private val _movie = mutableStateOf<MovieItem?>(null)
    val movie: State<MovieItem?> = _movie

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    private val _isFavor = mutableStateOf(false)
    val isFavor: State<Boolean> = _isFavor

    fun loadMovie(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                _movie.value = model.getFilm(id = id)
                _isFavor.value = favModel.isFavor(id)
            } catch (e: Exception) {
                _error.value = e.message ?: "Loading error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleFavor() {
        viewModelScope.launch {
            _movie.value?.let { movie ->
                if (_isFavor.value) {
                    favModel.removeFromFavor(movie)
                } else {
                    favModel.addToFavor(movie)
                }
                _isFavor.value = !_isFavor.value
            }
        }
    }
}