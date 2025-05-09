package com.example.filmfinder.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmfinder.data.MovieItem
import com.example.filmfinder.model.FavModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FavoritesViewModel(private val model: FavModel) : ViewModel() {
    private val _favorMovies = mutableStateOf<List<MovieItem.Movie>?>(null)
    val favorMovies: State<List<MovieItem.Movie>?> = _favorMovies

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error
    private val _previewPosterBiteArray = mutableStateOf<Map<Int, ByteArray?>>(emptyMap())
    val previewPosterBiteArray: State<Map<Int, ByteArray?>> = _previewPosterBiteArray

    private var job: Job? = null

    fun loadFavors() {
        job?.cancel()
        job = viewModelScope.launch {
            _error.value = null
            try {
                model.getFavors().collect { movies ->
                    _favorMovies.value = movies
                    val posters = mutableMapOf<Int, ByteArray?>()
                    movies.forEach { movie ->
                        posters[movie.id] = model.getMoviePosterBytes(movie.id)
                    }
                    _previewPosterBiteArray.value = posters
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Loading error"
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}

