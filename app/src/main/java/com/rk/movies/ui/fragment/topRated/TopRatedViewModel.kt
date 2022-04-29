package com.rk.movies.ui.fragment.topRated

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

class TopRatedViewModel(private val repository: Repository,
                        private val globalClass: GlobalClass
): ViewModel() {

    private var tag = "TopRatedViewModel"

    private var page = 1

    private val _topRatedList: MutableLiveData<List<Result>> = MutableLiveData()
    val topRatedList: LiveData<List<Result>>
        get() = _topRatedList

    val errorRes = MutableLiveData<String>()

    init {
        getTopRated()
    }

    fun getTopRated() {

        try {
            viewModelScope.launch {

                val response = repository.getTopRated(page)
                if(response.isSuccessful) {

                    response.body()?.let { res ->

                        globalClass.log(tag,"page: ${res.page}")
                        if(page == 1) {
                            _topRatedList.postValue(res.results)
                        }
                        else {
                            addDataToList(res.results)
                        }
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

        val list: MutableList<Result> = topRatedList.value as MutableList<Result>

        for(i in 0 until results.size) {
            val model = results.get(i)
            list.add(model)
        }

        _topRatedList.postValue(list)
    }
}