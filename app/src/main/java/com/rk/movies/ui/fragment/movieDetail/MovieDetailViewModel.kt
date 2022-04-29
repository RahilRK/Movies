package com.rk.movies.ui.fragment.movieDetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.rk.movies.model.ApiErrorMessage
import com.rk.movies.model.movieDetail.MovieDetailRes
import com.rk.movies.util.Constant.response_error
import com.rk.movies.util.GlobalClass
import com.rk.movies.util.Repository
import kotlinx.coroutines.launch

class MovieDetailViewModel(private val repository: Repository,
                           private val globalClass: GlobalClass
): ViewModel() {

    var tag = "MovieDetailViewModel"

    private val _movieDetailRes: MutableLiveData<MovieDetailRes> = MutableLiveData()
    val movieDetailRes: LiveData<MovieDetailRes>
    get() = _movieDetailRes

    val errorRes = MutableLiveData<String>()

    fun getMovieDetail(movieId: Int) {

        try {

            viewModelScope.launch {

                val response = repository.getMovieDetail(movieId)
                if(response.isSuccessful) {

                    response.body()?.let { res ->

                        _movieDetailRes.postValue(res)


                    }?:run {

                        val error = response_error
                        globalClass.log(tag,error)
                        errorRes.postValue(error)
                    }
                }
                else {

                    val gson = Gson()
                    val apiErrorModel: ApiErrorMessage = gson.fromJson(
                        response.errorBody()!!.charStream(),
                        ApiErrorMessage::class.java
                    )

                    val error = apiErrorModel.message
                    globalClass.log(tag,error)
                    errorRes.postValue(error)
                }
            }
        }
        catch (e: Exception) {

            val error = Log.getStackTraceString(e)
            globalClass.log(tag,error)
            errorRes.postValue(error)
        }
    }
}