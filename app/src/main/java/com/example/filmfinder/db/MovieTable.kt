package com.example.filmfinder.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.filmfinder.data.MovieItem

@Entity(tableName = "favorite_movies")
data class MovieEntity(
    @PrimaryKey(autoGenerate = false)
    val movieId: Int,
    @ColumnInfo("nameRu")
    val movieName: String?,
    @ColumnInfo("nameOriginal")
    val nameOriginal: String?,
    @ColumnInfo("year")
    val year: String?,
    @ColumnInfo("description")
    val description: String?
) {
    companion object {
        fun fromMovieItem(movie: MovieItem): MovieEntity {
            return MovieEntity(
                movieId = movie.id,
                movieName = movie.name,
                nameOriginal = movie.nameOriginal,
                year = movie.year,
                description = movie.description
            )
        }
    }
        fun toMovieItem():MovieItem.Movie{
            return MovieItem.Movie(
                id= this.movieId,
                name = this.movieName,
                nameOriginal=this.nameOriginal,
                description = this.description,
                year = this.year
            )
        }

}