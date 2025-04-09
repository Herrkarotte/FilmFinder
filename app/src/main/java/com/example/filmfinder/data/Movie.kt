package com.example.filmfinder.data

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("kinopoiskId") val id: Int,
    @SerializedName("nameRu") val name: String?,
    @SerializedName("posterUrlPreview") val posterUrlPreview: String?,
    @SerializedName("posterUrl") val posterUrl: String?,
    @SerializedName("year") val year: Int?,
    @SerializedName("description") val description: String?,
    @SerializedName("countries") val countries: List<Countries>,
    @SerializedName("genres") val genres: List<Genres>
)

data class Genres(
    @SerializedName("genre") val genre: String?
)

data class Countries(
    @SerializedName("country") val country: String?
)

data class MovieList(
    @SerializedName("items") val movies: List<Movie>,
    @SerializedName("totalPages") val pagesCount: Int
)