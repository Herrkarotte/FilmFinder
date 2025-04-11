package model

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.filmfinder.api.KinopoiskApiService
import com.example.filmfinder.data.Movie

class PagingSource(private val apiService: KinopoiskApiService) : PagingSource<Int, Movie>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val page = params.key ?: 1
            val response = apiService.getFilmsList(
                apiKey = "e30ffed0-76ab-4dd6-b41f-4c9da2b2735b",
                page = page
            )
            LoadResult.Page(
                data = response.movies,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.movies.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1) ?: state.closestPageToPosition(
                position
            )?.nextKey?.minus(1)
        }
    }
}