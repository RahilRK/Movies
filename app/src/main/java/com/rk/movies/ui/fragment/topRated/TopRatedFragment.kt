package com.rk.movies.ui.fragment.topRated

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rk.movies.R
import com.rk.movies.model.nowPlaying.Result
import com.rk.movies.util.GlobalClass
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_top_rated.*

@AndroidEntryPoint
class TopRatedFragment : Fragment(R.layout.fragment_top_rated) {

    var TAG = "TopRatedFragment"
    private lateinit var activity: Context

    @Inject lateinit var globalClass: GlobalClass
    private val viewModel: TopRatedViewModel by viewModels()

    var isLoading = false
    var offset = 0

    private var arrayList = arrayListOf<Result>()
    var adapter: TopRatedAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = requireContext()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar()
        onClick()
        setAdapter(arrayList)
        observeData()
    }

    private fun setToolbar() {
        //        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.title = resources.getString(R.string.top_rated)
    }

    private fun onClick() {
        recyclerView.addOnScrollListener(scrollListener)
    }

    private val scrollListener =
            object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                    //            globalClass.log(TAG,"lastVisibleItem:${lastVisibleItem}")

                    if (lastVisibleItem == offset - 1 && !isLoading) {
                        offset = lastVisibleItem + 1
                        showProgressBar()
                        viewModel.getTopRated()
                    }
                }
            }

    private fun observeData() {

        viewModel.topRatedList.observe(viewLifecycleOwner, observer)
    }

    private val observer =
            Observer<List<Result>> { list ->
                arrayList = list as ArrayList<Result>
                hideProgressBar()

                adapter?.addAll(arrayList)

                offset = arrayList.size
                globalClass.log(TAG, "offset: $offset")
            }

    private fun setAdapter(arrayList: ArrayList<Result>) {

        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter =
                TopRatedAdapter(
                        activity,
                        arrayList,
                        object : TopRatedAdapter.movieListOnClick {
                            override fun openDetail(pos: Int, movieId: Int) {
                                val action =
                                        TopRatedFragmentDirections
                                                .actionTopRatedFragmentToMovieDetailFragment(
                                                        movieId
                                                )
                                findNavController().navigate(action)
                            }
                        }
                )
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
