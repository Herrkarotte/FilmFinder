package com.example.filmfinder.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import coil3.ImageLoader
import com.example.filmfinder.db.MovieDao
import com.example.filmfinder.model.FavModel
import com.example.filmfinder.model.FilmModel
import com.example.filmfinder.retrofit.Retrofit
import com.example.filmfinder.viewmodel.MainViewModel

class MainViewModelFactory(private val dao: MovieDao, private val context: Context) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            val imageLoader = ImageLoader.Builder(context).build()
            return MainViewModel(
                FilmModel(
                    Retrofit.api
                ), FavModel(dao, imageLoader, context)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}