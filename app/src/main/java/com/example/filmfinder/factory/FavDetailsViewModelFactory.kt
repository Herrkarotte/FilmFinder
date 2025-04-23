package com.example.filmfinder.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.filmfinder.db.MovieDao
import com.example.filmfinder.model.FavModel
import com.example.filmfinder.viewmodel.FavDetailsViewModel

class FavDetailsViewModelFactory(private val dao: MovieDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavDetailsViewModel::class.java)) return FavDetailsViewModel(
            FavModel(dao)
        ) as T
        throw IllegalArgumentException("Unknown ViewModel")
    }
}