package com.rk.movies.ui.fragment.nowPlaying

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rk.movies.R
import com.rk.movies.model.nowPlaying.Result
import com.rk.movies.ui.SecondActivity
import com.rk.movies.util.Application
import com.rk.movies.util.GlobalClass
import com.rk.movies.util.Repository
import kotlinx.android.synthetic.main.fragment_now_playing.*

class NowPlayingFragment : Fragment(R.layout.fragment_now_playing) {

    var TAG = "NowPlayingFragment"
    private lateinit var activity: Context

    lateinit var globalClass: GlobalClass
    lateinit var repository: Repository
    lateinit var viewModel: NowPlayingViewModel

    var isLoading = false
    var offset = 0

    private var arrayList = arrayListOf<Result>()
    var adapter: NowPlayingAdapter? = null;

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
        viewModel = ViewModelProvider(this, NowPlayingViewModelFactory(repository, globalClass))
            .get(NowPlayingViewModel::class.java)
    }

    private fun setToolbar() {
//        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.title = resources.getString(R.string.now_playing)
    }

    private fun onClick() {

        edSearchMovie.setOnClickListener {
            startActivity(Intent(activity,SecondActivity::class.java))
        }

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
                viewModel.getNowPlaying()
            }
        }
    }

    private fun observeData() {

        viewModel.nowPlayingRes.observe(viewLifecycleOwner) { list ->

            arrayList = list as ArrayList<Result>
            hideProgressBar()

            adapter?.addAll(arrayList)

            offset = arrayList.size
            globalClass.log(TAG, "offset: $offset")
        }
    }

    private fun setAdapter(arrayList: ArrayList<Result>) {

        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = NowPlayingAdapter(activity, arrayList, object : NowPlayingAdapter.movieListOnClick {
            override fun openDetail(pos: Int, movieId: Int) {
                val action = NowPlayingFragmentDirections.actionNowPlayingFragmentToMovieDetailFragment(movieId)
                findNavController().navigate(action)
            }
        })
        recyclerView.adapter = adapter
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