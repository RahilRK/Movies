package com.rk.movies.util

import com.rk.movies.network.RetrofitClient
import com.rk.movies.util.Constant.APIKEY
import com.rk.movies.util.Constant.language

class Repository(private val globalClass: GlobalClass) {

    suspend fun getNowPlaying(page: Int) =
        RetrofitClient.apiCall.getNowPlaying(
            APIKEY,
            language,
            page)

    suspend fun getTopRated(page: Int) =
        RetrofitClient.apiCall.getTopRated(
            APIKEY,
            language,
            page)

    suspend fun doSearching(page: Int, searchKeyWord: String) =
        RetrofitClient.apiCall2.doSearching(
            APIKEY,
            language,
            page,
            false,
            searchKeyWord)

    suspend fun getMovieDetail(movieId: Int) =
        RetrofitClient.apiCall.getMovieDetail(
            movieId,
            APIKEY,
            language)

    suspend fun getUpcomingMovies(page: Int) =
        RetrofitClient.apiCall.getUpcomingMovies(
            APIKEY,
            language,
            page)
}