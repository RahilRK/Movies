package com.rk.movies.ui.fragment.nowPlaying

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

class NowPlayingViewModel(private val repository: Repository,
                          private val globalClass: GlobalClass
): ViewModel() {

    var tag = "NowPlayingViewModel"

    var page = 1

    private val _nowPlayingRes: MutableLiveData<List<Result>> = MutableLiveData()
    val nowPlayingRes: LiveData<List<Result>>
    get() = _nowPlayingRes

    val errorRes = MutableLiveData<String>()

    init {
        getNowPlaying()
    }

    fun getNowPlaying() {

        globalClass.log(tag,"getNowPlaying")
        try {
            /*val response = repository.getNowPlaying(language,page)
            response.enqueue(object : Callback<NowPlayingRes> {
                override fun onResponse(call: Call<NowPlayingRes>, response: Response<NowPlayingRes>) {

                    response.body()?.let {
                        nowPlayingRes.postValue(response.body())
                    }?:run {
                        errorRes.postValue(response_error)
                    }
                }

                override fun onFailure(call: Call<NowPlayingRes>, t: Throwable) {

                    val error = t.toString()
                    globalClass.log(tag,error)
                    errorRes.postValue(error)
                }
            })*/

            viewModelScope.launch {

                val response = repository.getNowPlaying(page)
                if(response.isSuccessful) {

                    response.body()?.let { res ->

                        globalClass.log(tag,"page: ${res.page}")
                        if(page == 1) {
                            _nowPlayingRes.postValue(res.results)
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

        val list: MutableList<Result> = nowPlayingRes.value as MutableList<Result>

        for(i in 0 until results.size) {
            val model = results.get(i)
            list.add(model)
        }

        _nowPlayingRes.postValue(list)
    }
}