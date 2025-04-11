package com.example.filmfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import model.FilmModel

class MainViewModel(private val model: FilmModel) : ViewModel() {
    val filmsPagingFlow = model.getFilms().cachedIn(viewModelScope)
}