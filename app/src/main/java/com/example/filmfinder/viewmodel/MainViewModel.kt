package com.example.filmfinder.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.filmfinder.model.FavModel
import com.example.filmfinder.model.FilmModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class MainViewModel(private val model: FilmModel, private val favModel: FavModel) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()



    private val _snackBarMessage = mutableStateOf<String?>(null)
    val snackBarMessage: State<String?> = _snackBarMessage

    val filmsPagingFlow = searchQuery.debounce(300).flatMapLatest { query ->
        if (query.isBlank()) {
            model.getFilms()
        } else {
            model.searchFilms(query)
        }
    }.cachedIn(viewModelScope)

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun toggleFavor(movieId: Int) {
        viewModelScope.launch {
            val movie = model.getFilm(movieId)
            if (favModel.isFavor(movieId)) {
                favModel.removeFromFavor(movie)
                _snackBarMessage.value = "Удалено из избранного"
            } else {
                favModel.addToFavor(movie, true)
                _snackBarMessage.value = "Добавлено в избранное"
            }
        }
    }

    fun clearSnackbar() {
        _snackBarMessage.value = null
    }
}
