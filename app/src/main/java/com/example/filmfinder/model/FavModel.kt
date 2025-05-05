package com.example.filmfinder.model

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import coil3.BitmapImage
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.size.Size
import com.example.filmfinder.data.MovieItem
import com.example.filmfinder.db.MovieDao
import com.example.filmfinder.db.MovieEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream


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
            val (posterBytes, previewPosterBytes) = if (savePoster && (!movie.posterUrl.isNullOrEmpty() || !movie.posterUrlPreview.isNullOrEmpty())) {
                try {
                    val poster = async { movie.posterUrl?.let { loadImageBytes(it) } }
                    val previewPoster = async { movie.posterUrlPreview?.let { loadImageBytes(it) } }
                    Pair(poster.await(), previewPoster.await())
                } catch (e: Exception) {
                    Pair(null, null)
                }
            } else {
                Pair(null, null)
            }
            movieDao.insert(MovieEntity.fromMovieItem(movie, posterBytes, previewPosterBytes))
        }
    }

    private suspend fun loadImageBytes(url: String): ByteArray? {
        return withContext(Dispatchers.IO) {
            try {
                val request = ImageRequest.Builder(context).data(url).size(Size.ORIGINAL).build()

                val imageResult = imageLoader.execute(request)
                val image = imageResult.image
                if (image is BitmapImage) {
                    val bitmap = image.bitmap
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
                    stream.toByteArray()
                } else {
                    null
                }
            } catch (e: Exception) {
                Log.e("FavModel", "Error loading image bytes", e)
                null
            }
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