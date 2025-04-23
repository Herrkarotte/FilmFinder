package com.example.filmfinder.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.filmfinder.data.Countries
import com.example.filmfinder.data.Genres
import com.example.filmfinder.data.MovieItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "favorite_movies")
data class MovieEntity(
    @PrimaryKey(autoGenerate = false) val movieId: Int,
    @ColumnInfo("nameRu") val movieName: String?,
    @ColumnInfo("nameOriginal") val nameOriginal: String?,
    @ColumnInfo("year") val year: String?,
    @ColumnInfo("description") val description: String?,
    @ColumnInfo("countries") val countries: String?,
    @ColumnInfo("genres") val genres: String?
) {
    companion object {
        fun fromMovieItem(movie: MovieItem): MovieEntity {
            val countriesList = movie.countries.let { Gson().toJson(it) }
            val genresList = movie.genres.let { Gson().toJson(it) }
            return MovieEntity(
                movieId = movie.id,
                movieName = movie.name,
                nameOriginal = movie.nameOriginal,
                year = movie.year,
                description = movie.description,
                genres = genresList,
                countries = countriesList
            )
        }
    }

    fun toMovieItem(): MovieItem.Movie {
        val type = object : TypeToken<List<Countries>>() {}.type
        val countriesList: List<Countries> =
            countries?.let { Gson().fromJson(it, type) } ?: emptyList()
        val genretype = object : TypeToken<List<Genres>>() {}.type
        val genresList: List<Genres> = genres?.let { Gson().fromJson(it, genretype) } ?: emptyList()

        return MovieItem.Movie(
            id = this.movieId,
            name = this.movieName,
            nameOriginal = this.nameOriginal,
            description = this.description,
            year = this.year,
            countries = countriesList,
            genres = genresList
        )
    }

}