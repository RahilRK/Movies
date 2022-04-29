package com.rk.movies.ui.fragment.movieDetail

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.rk.movies.R
import com.rk.movies.model.nowPlaying.Result
import com.rk.movies.ui.fragment.topRated.TopRatedViewModel
import com.rk.movies.ui.fragment.topRated.TopRatedViewModelFactory
import com.rk.movies.util.Application
import com.rk.movies.util.Constant
import com.rk.movies.util.GlobalClass
import com.rk.movies.util.Repository
import kotlinx.android.synthetic.main.fragment_movie_detail.*
import kotlinx.android.synthetic.main.fragment_movie_detail.view.*
import kotlinx.android.synthetic.main.fragment_now_playing.*
import kotlinx.android.synthetic.main.fragment_now_playing.toolbar
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MovieDetailFragment : Fragment(R.layout.fragment_movie_detail) {

    var TAG = "MovieDetailFragment"
    private lateinit var activity: Context

    lateinit var globalClass: GlobalClass
    lateinit var repository: Repository
    lateinit var viewModel: MovieDetailViewModel

    val args: MovieDetailFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = requireContext()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        setToolbar()
        viewModel.getMovieDetail(args.movieId)
        observeData()
    }

    private fun init() {
        globalClass = (requireActivity().application as Application).globalClass
        repository = (requireActivity().application as Application).repository
        viewModel = ViewModelProvider(this, MovieDetailViewModelFactory(repository, globalClass))
            .get(MovieDetailViewModel::class.java)
    }

    private fun setToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.title = ""
        toolbar.setNavigationOnClickListener {
            getActivity()?.onBackPressed()
        }
    }

    private fun observeData() {

        viewModel.movieDetailRes.observe(viewLifecycleOwner) { model ->

            globalClass.log(TAG, "detail: $model")
            Glide.with(activity)
                .load("${Constant.BASE_IMAGE_URL}${model.poster_path}")
                .apply(RequestOptions.bitmapTransform(RoundedCorners(16)))
                .into(poster_path)

            original_title.text = "${model.original_title}"
            vote_average.rating = (model.vote_average/2).toFloat()

            if(model.overview.isNotEmpty() && model.overview.length > 60) {
                addReadMore(model.overview, overview)
            }
            else {
                overview.text = model.overview
            }

            val setReleaseYear = changedateTimeFormat("yyyy-mm-dd","YYYY",model.release_date)
            release_date.text = setReleaseYear

            val runTime: Double = (model.runtime/60).toDouble()
            runtime.text = "$runTime hr"

            val productionCompaniesList = mutableListOf<String>()
            for(name in model.production_companies) {
                productionCompaniesList.add(name.name)
            }

            if(productionCompaniesList.isNotEmpty()) {
                production_companies.text = android.text.TextUtils.join(", ", productionCompaniesList)
            }
        }
    }

    private fun addReadMore(text: String, textView: TextView) {
        val ss = SpannableString(text.substring(0, 60) + "... read more")
        val clickableSpan: ClickableSpan = object : ClickableSpan() {

            override fun onClick(view: View) {
                addReadLess(text, textView)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ds.color = activity.getColor(R.color.purple_200)
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        ds.color = activity.getColor(R.color.purple_200)
                    }
                }
            }
        }
        ss.setSpan(clickableSpan, ss.length - 10, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = ss
        textView.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun addReadLess(text: String, textView: TextView) {
        val ss = SpannableString("$text read less")
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                addReadMore(text, textView)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ds.color = activity.getColor(R.color.purple_200)
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        ds.color = activity.getColor(R.color.purple_200)
                    }
                }
            }
        }
        ss.setSpan(clickableSpan, ss.length - 10, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = ss
        textView.movementMethod = LinkMovementMethod.getInstance()
    }

    fun changedateTimeFormat(
        defaultPattern: String,
        neededPattern: String,
        input: String?
    ): String? {

//        String inputPattern = "yyyy-mm-dd";
//        String outputPattern = "dd/mm/yyyy";
        val inputFormat = SimpleDateFormat(defaultPattern)
        val outputFormat = SimpleDateFormat(neededPattern)
        var date: Date? = null
        var str: String? = null
        try {
            date = inputFormat.parse(input)
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return str
    }
}