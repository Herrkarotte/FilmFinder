package com.example.filmfinder.api

import com.example.filmfinder.data.MovieData
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface KinopoiskApiService {
    @GET("/api/v2.2/films/{id}")
    suspend fun getFilm(
        @Path("id") id: Int,
        @Header("X-API-KEY") apiKey: String
    ): MovieData
}