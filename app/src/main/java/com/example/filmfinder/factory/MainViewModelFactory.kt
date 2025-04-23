package com.example.filmfinder.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.filmfinder.db.MovieDao
import com.example.filmfinder.model.FavModel
import com.example.filmfinder.model.FilmModel
import com.example.filmfinder.retrofit.Retrofit
import com.example.filmfinder.viewmodel.MainViewModel

class MainViewModelFactory(private val dao: MovieDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) return MainViewModel(
            FilmModel(
                Retrofit.api
            ), FavModel(dao)
        ) as T
        throw IllegalArgumentException("Unknown ViewModel")
    }
}