package com.rk.movies.ui.fragment.searchMovie

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.rk.movies.model.ApiErrorMessage
import com.rk.movies.model.nowPlaying.Result
import com.rk.movies.util.Constant.response_error
import com.rk.movies.util.GlobalClass
import com.rk.movies.util.Repository
import kotlinx.coroutines.launch

class SearchMovieViewModel(private val repository: Repository,
                           private val globalClass: GlobalClass
): ViewModel() {

    var tag = "SearchMovieViewModel"

    var page = 1

    private val _searchRes: MutableLiveData<List<Result>> = MutableLiveData()
    val searchRes: LiveData<List<Result>>
    get() = _searchRes

    val errorRes = MutableLiveData<String>()

    val _searchKeyWord: MutableLiveData<String> = MutableLiveData()

    val emptyList: List<Result> = emptyList()

    fun doSearching() {

        globalClass.log(tag,"doSearching")
        _searchRes.postValue(emptyList)
        page = 1

        try {

            viewModelScope.launch {

                val response = repository.doSearching(page, _searchKeyWord.value!!)
                if(response.isSuccessful) {

                    response.body()?.let { res ->

                        globalClass.log(tag,"page: ${res.page}")
                        _searchRes.postValue(res.results)
                        page++

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

    fun loadMore() {

        globalClass.log(tag,"loadMore")
        try {

            viewModelScope.launch {

                val response = repository.doSearching(page, _searchKeyWord.value!!)
                if(response.isSuccessful) {

                    response.body()?.let { res ->

                        globalClass.log(tag,"page: ${res.page}")
                        addDataToList(res.results)
                        page++

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

    fun addDataToList(results: ArrayList<Result>) {

        val list: MutableList<Result> = searchRes.value as MutableList<Result>

        for(i in 0 until results.size) {
            val model = results.get(i)
            list.add(model)
        }

        _searchRes.postValue(list)
    }
}