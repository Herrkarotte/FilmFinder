package com.example.filmfinder.model

import com.example.filmfinder.data.MovieItem
import com.example.filmfinder.db.MovieDao
import com.example.filmfinder.db.MovieEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavModel(private val movieDao: MovieDao) {
    fun getFavors(): Flow<List<MovieItem.Movie>> {
        val response = movieDao.getAllMovies().map { movie -> movie.map { it.toMovieItem() } }
        return response
    }

    suspend fun getFavoriteMovie(movieId: Int): MovieItem.Movie? {
        return movieDao.getMovieById(movieId)?.toMovieItem()
    }

    suspend fun addToFavor(movie: MovieItem) {
        movieDao.insert(MovieEntity.fromMovieItem(movie))
    }

    suspend fun removeFromFavor(movie: MovieItem) {
        movieDao.delete(MovieEntity.fromMovieItem(movie))
    }

    suspend fun isFavor(movieId: Int): Boolean = movieDao.isFavorite(movieId)
}