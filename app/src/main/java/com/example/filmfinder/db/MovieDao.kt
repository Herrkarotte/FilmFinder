package com.example.filmfinder.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.filmfinder.data.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert
    suspend fun insert(movie: Movie)

    @Delete
    suspend fun delete(movie: Movie)

    @Query("SELECT* FROM movie_table")
    suspend fun getAllMovies(): Flow<List<Movie>>
}