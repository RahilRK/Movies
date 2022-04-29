package com.rk.movies.ui.fragment.searchMovie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rk.movies.util.GlobalClass
import com.rk.movies.util.Repository

class SearchMovieViewModelFactory(private val repository: Repository,
                                  private val globalClass: GlobalClass) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchMovieViewModel(repository,globalClass) as T
    }
}