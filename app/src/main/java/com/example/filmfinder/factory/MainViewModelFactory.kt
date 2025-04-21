package com.example.filmfinder.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.filmfinder.retrofit.Retrofit
import com.example.filmfinder.model.FilmModel
import com.example.filmfinder.viewmodel.MainViewModel

class MainViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java))
            return MainViewModel(FilmModel(Retrofit.api)) as T
        throw IllegalArgumentException("Unknown ViewModel")
    }
}