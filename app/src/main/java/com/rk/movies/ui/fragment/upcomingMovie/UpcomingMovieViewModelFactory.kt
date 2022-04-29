package com.rk.movies.ui.fragment.upcomingMovie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rk.movies.util.GlobalClass
import com.rk.movies.util.Repository

class UpcomingMovieViewModelFactory(private val repository: Repository,
                                    private val globalClass: GlobalClass) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UpcomingMovieViewModel(repository,globalClass) as T
    }
}