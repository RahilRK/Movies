package com.rk.movies.ui.fragment.movieDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rk.movies.util.GlobalClass
import com.rk.movies.util.Repository

class MovieDetailViewModelFactory(private val repository: Repository,
                                  private val globalClass: GlobalClass) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MovieDetailViewModel(repository,globalClass) as T
    }
}