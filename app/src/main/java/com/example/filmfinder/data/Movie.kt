package com.example.filmfinder.data

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("kinopoiskId") val id: Int,
    @SerializedName("nameRu") val name: String?,
    @SerializedName("nameOriginal") val originalName: String?
)

data class MovieList(
    @SerializedName("items") val movies: List<Movie>,
    @SerializedName("totalPages") val pagesCount: Int
)