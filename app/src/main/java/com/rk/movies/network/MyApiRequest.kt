package com.rk.movies.network

import com.rk.movies.model.movieDetail.MovieDetailRes
import com.rk.movies.model.nowPlaying.NowPlayingRes
import retrofit2.Response
import retrofit2.http.*


interface MyApiRequest {

    @GET("now_playing")
    suspend fun getNowPlaying(
        @Query("api_key") api_key: String,
        @Query("language") language: String,
        @Query("page") page: Int,
    ) : Response<NowPlayingRes>

    @GET("top_rated")
    suspend fun getTopRated(
        @Query("api_key") api_key: String,
        @Query("language") language: String,
        @Query("page") page: Int,
    ) : Response<NowPlayingRes>

    @GET("movie")
    suspend fun doSearching(
        @Query("api_key") api_key: String,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("include_adult") include_adult: Boolean,
        @Query("query") query: String,
    ) : Response<NowPlayingRes>

    @GET("{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String,
        @Query("language") language: String,
    ) : Response<MovieDetailRes>

    @GET("upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") api_key: String,
        @Query("language") language: String,
        @Query("page") page: Int,
    ) : Response<NowPlayingRes>
}