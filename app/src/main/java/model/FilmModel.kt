package model

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.filmfinder.api.KinopoiskApiService
import com.example.filmfinder.data.Movie
import kotlinx.coroutines.flow.Flow

class FilmModel(private val api: KinopoiskApiService) {
    fun getFilms(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                PagingSource(api)
            }
        ).flow
    }

    suspend fun getFilm(id: Int): Movie {
        val response = api.getFilm(
            id = id, apiKey = "e30ffed0-76ab-4dd6-b41f-4c9da2b2735b"
        )
        return response
    }
}

