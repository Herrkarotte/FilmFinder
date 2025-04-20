package com.example.filmfinder.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: MovieEntity)

    @Delete
    suspend fun delete(movie: MovieEntity)

    @Query("SELECT * FROM favorite_movies")
    fun getAllMovies(): Flow<List<MovieEntity>>

    @Query("SELECT EXISTS(SELECT * FROM favorite_movies WHERE movieId = :movieId)")
    suspend fun isFavorite(movieId: Int): Boolean
}
