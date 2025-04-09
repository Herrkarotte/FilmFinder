package com.example.filmfinder.retrofit

import com.example.filmfinder.api.KinopoiskApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Retrofit {
    private const val BASE_URL = "https://kinopoiskapiunofficial.tech"
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val api: KinopoiskApiService by lazy {
        retrofit.create(KinopoiskApiService::class.java)
    }
}