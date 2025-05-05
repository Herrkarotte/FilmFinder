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
    @ColumnInfo("genres") val genres: String?,
    @ColumnInfo("posterUrl") val posterUrl: String?,
    @ColumnInfo("posterUrlPreview") val posterUrlPreview: String?,
    @ColumnInfo("posterBytes") val posterBytes: ByteArray?,
    @ColumnInfo("previewPosterBytes") val previewPosterBytes: ByteArray?
) {
    companion object {
        fun fromMovieItem(
            movie: MovieItem,
            posterBytes: ByteArray? = null,
            previewPosterBytes: ByteArray? = null
        ): MovieEntity {
            val countriesList = movie.countries.let { Gson().toJson(it) }
            val genresList = movie.genres.let { Gson().toJson(it) }
            return MovieEntity(
                movieId = movie.id,
                movieName = movie.name,
                nameOriginal = movie.nameOriginal,
                year = movie.year,
                description = movie.description,
                genres = genresList,
                countries = countriesList,
                posterUrl = movie.posterUrl,
                posterUrlPreview = movie.posterUrlPreview,
                posterBytes = posterBytes,
                previewPosterBytes = previewPosterBytes
            )
        }
    }

    fun toMovieItem(): MovieItem.Movie {
        val type = object : TypeToken<List<Countries>>() {}.type
        val countriesList: List<Countries> =
            countries?.let { Gson().fromJson(it, type) } ?: emptyList()
        val genreType = object : TypeToken<List<Genres>>() {}.type
        val genresList: List<Genres> = genres?.let { Gson().fromJson(it, genreType) } ?: emptyList()

        return MovieItem.Movie(
            id = this.movieId,
            name = this.movieName,
            nameOriginal = this.nameOriginal,
            description = this.description,
            year = this.year,
            countries = countriesList,
            genres = genresList,
            posterUrl = posterUrl,
            posterUrlPreview = posterUrlPreview
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MovieEntity

        if (posterBytes != null) {
            if (other.posterBytes == null) return false
            if (!posterBytes.contentEquals(other.posterBytes)) return false
        } else if (other.posterBytes != null) return false
        if (previewPosterBytes != null) {
            if (other.previewPosterBytes == null) return false
            if (!previewPosterBytes.contentEquals(other.previewPosterBytes)) return false
        } else if (other.previewPosterBytes != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = posterBytes?.contentHashCode() ?: 0
        result = 31 * result + (previewPosterBytes?.contentHashCode() ?: 0)
        return result
    }
}