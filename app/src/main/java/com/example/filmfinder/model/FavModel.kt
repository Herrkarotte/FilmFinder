package com.example.filmfinder.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import coil3.ImageLoader
import coil3.request.ImageRequest
import com.example.filmfinder.data.MovieItem
import com.example.filmfinder.db.MovieDao
import com.example.filmfinder.db.MovieEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FavModel(
    private val movieDao: MovieDao,
    private val imageLoader: ImageLoader,
    private val context: Context
) {
    fun getFavors(): Flow<List<MovieItem.Movie>> {
        val response = movieDao.getAllMovies().map { movie -> movie.map { it.toMovieItem() } }
        return response
    }

    suspend fun getFavoriteMovie(movieId: Int): MovieItem? {
        return withContext(Dispatchers.IO) {
            movieDao.getMovieById(movieId)?.toMovieItem()
        }
    }

    suspend fun addToFavor(movie: MovieItem, savePoster: Boolean = true) {
        withContext(Dispatchers.IO) {
            val posterBytes = if (savePoster && movie.posterUrl != null) {
                loadImageBytes(movie.posterUrl!!)
            } else {
                null
            }
            movieDao.insert(MovieEntity.fromMovieItem(movie, posterBytes))
        }
    }

    private suspend fun loadImageBytes(url: String): ByteArray {
        return suspendCoroutine { countinuation ->
            val request = ImageRequest.Builder(context)
                .data(url)
                .target { drawable ->
                    val bitmap = (drawable as BitmapDrawable).bitmap
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
                    countinuation.resume(stream.toByteArray())
                }.build()
            imageLoader.enqueue(request)
        }
    }

    suspend fun getMoviePosterBytes(movieId: Int): ByteArray? {
        return withContext(Dispatchers.IO) {
            movieDao.getMovieById(movieId)?.posterBytes
        }
    }

    suspend fun removeFromFavor(movie: MovieItem) {
        withContext(Dispatchers.IO) {
            movieDao.delete(MovieEntity.fromMovieItem(movie))
        }
    }

    suspend fun isFavor(movieId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            movieDao.isFavorite(movieId)
        }
    }
}