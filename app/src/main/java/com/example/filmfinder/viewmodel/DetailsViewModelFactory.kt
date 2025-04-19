package com.example.filmfinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.filmfinder.retrofit.Retrofit
import com.example.filmfinder.model.FilmModel

class DetailsViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
            return DetailsViewModel(FilmModel(Retrofit.api)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}