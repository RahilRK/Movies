package com.rk.movies.ui.fragment.searchMovie

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rk.movies.R
import com.rk.movies.model.nowPlaying.Result
import com.rk.movies.util.Application
import com.rk.movies.util.GlobalClass
import com.rk.movies.util.Repository
import kotlinx.android.synthetic.main.fragment_now_playing.paginationProgressBar
import kotlinx.android.synthetic.main.fragment_now_playing.recyclerView
import kotlinx.android.synthetic.main.fragment_now_playing.toolbar
import kotlinx.android.synthetic.main.fragment_search_movie.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchMovieFragment : Fragment(R.layout.fragment_search_movie) {

    var TAG = "SearchMovieFragment"
    private lateinit var activity: Context

    lateinit var globalClass: GlobalClass
    lateinit var repository: Repository
    lateinit var viewModel: SearchMovieViewModel

    var isLoading = false
    var doSearch = false
    var offset = 0

    private var arrayList = arrayListOf<Result>()
    var adapter: SearchMovieAdapter? = null;

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = requireContext()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        setToolbar()
        onClick()
        setAdapter(arrayList)
        observeData()
    }

    private fun init() {
        globalClass = (requireActivity().application as Application).globalClass
        repository = (requireActivity().application as Application).repository
        viewModel = ViewModelProvider(this, SearchMovieViewModelFactory(repository, globalClass))
            .get(SearchMovieViewModel::class.java)
    }

    private fun setToolbar() {
//        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.title = resources.getString(R.string.search_movie)
    }

    private fun onClick() {

        globalClass.requestFocus(edSearchMovie)

        /*var job: Job? = null
        edSearchMovie.addTextChangedListener { editable ->

            job?.cancel()

            job = MainScope().launch {
                delay(1000L)
                editable?.let {
                    if(editable.toString().isNotBlank()) {

                        viewModel._searchKeyWord.value = it.toString()
                        offset = 0
                        doSearch = true
                        viewModel.doSearching()
                    }
                    else {
                        adapter?.clearAll()
                        showHideLayout()
                    }
                }
            }
        }*/

        var job: Job? = null
        edSearchMovie.addTextChangedListener(object : TextWatcher{

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(text: Editable?) {

                job?.cancel()
                job = MainScope().launch {
                    delay(1000L)
                    text?.let {

                        if(it.isNotBlank()) {

                            viewModel._searchKeyWord.value = it.toString()
                            offset = 0
                            doSearch = true
                            viewModel.doSearching()
                        }
                        else {
                            adapter?.clearAll()
//                            showHideLayout()
                        }

                    }?:kotlin.run {

                    }
                }
            }
        })

        edSearchMovie.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                globalClass.hidekeyboard(edSearchMovie)
                return@OnEditorActionListener true
            }
            false
        })
        recyclerView.addOnScrollListener(scrollListener)
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
//            globalClass.log(TAG,"lastVisibleItem:${lastVisibleItem}")

            if (lastVisibleItem == offset - 1 && !isLoading) {
                offset = lastVisibleItem + 1
                showProgressBar()
                viewModel.loadMore()
            }
        }
    }

    private fun observeData() {

        viewModel.searchRes.observe(viewLifecycleOwner) { list ->

            if(list.isNotEmpty()) {

                if(doSearch) {
                    adapter?.clearAll()
                    doSearch = false
                }

                arrayList = list as ArrayList<Result>
                hideProgressBar()

                adapter?.addAll(arrayList)

                offset = arrayList.size
                globalClass.log(TAG, "offset: $offset")
                globalClass.log(TAG, "title: ${arrayList.get(0).title}")
            }

//            showHideLayout()
        }
    }

    private fun setAdapter(arrayList: ArrayList<Result>) {

        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = SearchMovieAdapter(activity, arrayList)
        recyclerView.adapter = adapter
    }

    private fun showHideLayout() {
        adapter?.let {
            if(it.list.isEmpty()) {
                recyclerViewLayout.visibility = View.GONE
                noDataLayout.visibility = View.VISIBLE
                globalClass.log(TAG,"isEmpty")
            }
            else {
                noDataLayout.visibility = View.GONE
                recyclerViewLayout.visibility = View.VISIBLE
                globalClass.log(TAG,"isNotEmpty")
            }
        }
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.GONE
        isLoading = false
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }
}