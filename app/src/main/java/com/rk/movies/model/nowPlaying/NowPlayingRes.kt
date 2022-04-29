package com.rk.movies.model.nowPlaying

data class NowPlayingRes(
    val page: Int,
    val results: ArrayList<Result>,
    val total_pages: Int,
    val total_results: Int
)