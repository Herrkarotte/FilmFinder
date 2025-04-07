package com.example.filmfinder.data

import com.google.gson.annotations.SerializedName

data class MovieData(
    @SerializedName("kinopoiskId") val id: Int,
    @SerializedName("nameRu")val name: String?,
    @SerializedName("ratingGoodReview")val ratingGoodReview: Double?
)