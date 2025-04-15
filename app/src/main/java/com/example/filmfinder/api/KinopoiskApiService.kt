package com.example.filmfinder.api

import com.example.filmfinder.data.MovieItem
import com.example.filmfinder.data.MovieListItem
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface KinopoiskApiService {
    @GET("/api/v2.2/films/{id}")
    suspend fun getFilm(
        @Path("id") id: Int,
        @Header("X-API-KEY") apiKey: String
    ): MovieItem.Movie

    @GET("/api/v2.2/films/collections")
    suspend fun getFilmsList(
        @Header("X-API-KEY") apiKey: String,
        @Query("page") page: Int,
        @Query("type") type: String = "TOP_POPULAR_ALL"
    ): MovieListItem.MovieList

    @GET("/api/v2.1/films/search-by-keyword")
    suspend fun searchFilms(
        @Header("X-API-KEY") apiKey: String,
        @Query("page") page: Int,
        @Query("keyword") keyword: String?
    ): MovieListItem.MovieList2
}
