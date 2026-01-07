package com.rk.movies.repository

import com.rk.movies.model.movieDetail.MovieDetailRes
import com.rk.movies.model.nowPlaying.NowPlayingRes
import retrofit2.Response

interface IRepository {
    suspend fun getNowPlaying(page: Int): Response<NowPlayingRes>
    suspend fun getTopRated(page: Int): Response<NowPlayingRes>
    suspend fun doSearching(page: Int, searchKeyWord: String): Response<NowPlayingRes>
    suspend fun getMovieDetail(movieId: Int): Response<MovieDetailRes>
    suspend fun getUpcomingMovies(page: Int): Response<NowPlayingRes>
}
