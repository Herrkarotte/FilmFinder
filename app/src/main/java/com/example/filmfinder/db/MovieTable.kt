package com.example.filmfinder.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_table")
data class MovieTable(
    @PrimaryKey(autoGenerate = false)
    var movieId: Int,
    @ColumnInfo("nameRu")
    var movieName: String = "",
    @ColumnInfo("year")
    var year: Int,
    @ColumnInfo("description")
    var description: String = ""
)