package com.example.filmfinder.api

import com.example.filmfinder.data.Movie
import com.example.filmfinder.data.MovieList
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface KinopoiskApiService {
    @GET("/api/v2.2/films/{id}")
    suspend fun getFilm(
        @Path("id") id: Int,
        @Header("X-API-KEY") apiKey: String
    ): Movie

    @GET("/api/v2.2/films/collections")
    suspend fun getFilmsList(
        @Header("X-API-KEY") apiKey: String,
        @Query("page") page: Int,
        @Query("type") type: String = "TOP_POPULAR_ALL"
    ): MovieList
}
