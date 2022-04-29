package com.rk.movies.ui.fragment.nowPlaying

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rk.movies.util.GlobalClass
import com.rk.movies.util.Repository

class NowPlayingViewModelFactory(private val repository: Repository,
                                 private val globalClass: GlobalClass) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NowPlayingViewModel(repository,globalClass) as T
    }
}