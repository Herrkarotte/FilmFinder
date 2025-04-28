package com.example.filmfinder.data

import com.google.gson.annotations.SerializedName

sealed class MovieItem {
    abstract val id: Int
    abstract val name: String?
    abstract val nameOriginal: String?
     abstract val posterUrl: String?
    abstract val posterUrlPreview: String?
    abstract val year: String?
    abstract val description: String?
    abstract val countries: List<Countries>
    abstract val genres: List<Genres>


    data class Movie(
        @SerializedName("kinopoiskId") override val id: Int,
        @SerializedName("nameRu") override val name: String?,
        @SerializedName("nameOriginal") override val nameOriginal: String?,
        @SerializedName("posterUrlPreview") override val posterUrlPreview: String?,
        @SerializedName("posterUrl") override val posterUrl: String?,
        @SerializedName("year") override val year: String?,
        @SerializedName("description") override val description: String?,
        @SerializedName("countries") override val countries: List<Countries>,
        @SerializedName("genres") override val genres: List<Genres>
    ) : MovieItem()

    data class Movie2(
        @SerializedName("filmId") override val id: Int,
        @SerializedName("nameRu") override val name: String?,
        @SerializedName("nameEn") override val nameOriginal: String?,
        @SerializedName("posterUrlPreview") override val posterUrlPreview: String?,
        @SerializedName("posterUrl") override val posterUrl: String?,
        @SerializedName("year") override val year: String?,
        @SerializedName("description") override val description: String?,
        @SerializedName("countries") override val countries: List<Countries>,
        @SerializedName("genres") override val genres: List<Genres>
    ) : MovieItem()
}

data class Genres(
    @SerializedName("genre") val genre: String?
)

data class Countries(
    @SerializedName("country") val country: String?
)

sealed class MovieListItem {
    abstract val movies: List<MovieItem>
    abstract val pagesCount: Int

    data class MovieList(
        @SerializedName("items") override val movies: List<MovieItem.Movie>,
        @SerializedName("totalPages") override val pagesCount: Int
    ) : MovieListItem()

    data class MovieList2(
        @SerializedName("films") override val movies: List<MovieItem.Movie2>,
        @SerializedName("totalPages") override val pagesCount: Int
    ) : MovieListItem()
}
