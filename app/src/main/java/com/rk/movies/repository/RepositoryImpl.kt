package com.rk.movies.repository

import com.rk.movies.model.movieDetail.MovieDetailRes
import com.rk.movies.model.nowPlaying.NowPlayingRes
import com.rk.movies.network.MyApiRequest
import com.rk.movies.util.Constant.APIKEY
import com.rk.movies.util.Constant.language
import com.rk.movies.util.GlobalClass
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import retrofit2.Response

@Singleton
class RepositoryImpl
@Inject
constructor(
        private val globalClass: GlobalClass,
        @Named("MovieApi") private val movieApi: MyApiRequest,
        @Named("SearchApi") private val searchApi: MyApiRequest
) : IRepository {

    override suspend fun getNowPlaying(page: Int): Response<NowPlayingRes> =
            movieApi.getNowPlaying(APIKEY, language, page)

    override suspend fun getTopRated(page: Int): Response<NowPlayingRes> =
            movieApi.getTopRated(APIKEY, language, page)

    override suspend fun doSearching(page: Int, searchKeyWord: String): Response<NowPlayingRes> =
            searchApi.doSearching(APIKEY, language, page, false, searchKeyWord)

    override suspend fun getMovieDetail(movieId: Int): Response<MovieDetailRes> =
            movieApi.getMovieDetail(movieId, APIKEY, language)

    override suspend fun getUpcomingMovies(page: Int): Response<NowPlayingRes> =
            movieApi.getUpcomingMovies(APIKEY, language, page)
}
