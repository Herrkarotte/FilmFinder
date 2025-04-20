package com.example.filmfinder.model

import com.example.filmfinder.data.MovieItem
import com.example.filmfinder.db.MovieDao
import com.example.filmfinder.db.MovieEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavModel(private val movieDao: MovieDao) {
    fun getFavor(): Flow<List<MovieItem.Movie>> =
        movieDao.getAllMovies().map { list -> list.map { it.toMovieItem() } }

    suspend fun addToFavor(movie: MovieItem) {
        movieDao.insert(MovieEntity.fromMovieItem(movie))
    }

    suspend fun removeFromFavor(movie: MovieItem) {
        movieDao.delete(MovieEntity.fromMovieItem(movie))
    }

    suspend fun isFavor(movieId: Int): Boolean = movieDao.isFavorite(movieId)
}