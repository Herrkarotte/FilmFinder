package model

import com.example.filmfinder.api.KinopoiskApiService
import com.example.filmfinder.data.Movie
import com.example.filmfinder.data.MovieList

class FilmModel(private val api: KinopoiskApiService) {
    suspend fun getFilmList(): MovieList {
        val response = api.getFilmsList(
            apiKey = "e30ffed0-76ab-4dd6-b41f-4c9da2b2735b"
        )
        return response

    }

    suspend fun getFilm(id: Int): Movie {
        val response = api.getFilm(
            id = id, apiKey = "e30ffed0-76ab-4dd6-b41f-4c9da2b2735b"
        )
        return response
    }
}

