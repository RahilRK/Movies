package com.rk.movies.util

import com.rk.movies.network.MyApiRequest
import com.rk.movies.util.Constant.APIKEY
import com.rk.movies.util.Constant.language
import javax.inject.Inject
import javax.inject.Named

class Repository
@Inject
constructor(
        private val globalClass: GlobalClass,
        @Named("MovieApi") private val movieApi: MyApiRequest,
        @Named("SearchApi") private val searchApi: MyApiRequest
) {

    suspend fun getNowPlaying(page: Int) = movieApi.getNowPlaying(APIKEY, language, page)

    suspend fun getTopRated(page: Int) = movieApi.getTopRated(APIKEY, language, page)

    suspend fun doSearching(page: Int, searchKeyWord: String) =
            searchApi.doSearching(APIKEY, language, page, false, searchKeyWord)

    suspend fun getMovieDetail(movieId: Int) = movieApi.getMovieDetail(movieId, APIKEY, language)

    suspend fun getUpcomingMovies(page: Int) = movieApi.getUpcomingMovies(APIKEY, language, page)
}
