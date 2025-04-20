package com.example.filmfinder.viewmodel

import androidx.lifecycle.ViewModel
import com.example.filmfinder.model.FavModel

class FavDetailsModel(
    private val model:FavModel
):ViewModel() {
    val favor=model.getFavor()
}