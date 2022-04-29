package com.rk.movies.ui.fragment.topRated

import android.content.Context
import android.os.Build
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.rk.movies.R
import com.rk.movies.databinding.TopratedItemBinding
import com.rk.movies.model.nowPlaying.Result
import com.rk.movies.ui.fragment.nowPlaying.NowPlayingAdapter
import com.rk.movies.util.Constant.BASE_IMAGE_URL
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class TopRatedAdapter(
    private val activity: Context,
    private val list: ArrayList<Result>,
    private val onClick: TopRatedAdapter.movieListOnClick,
) : RecyclerView.Adapter<TopRatedAdapter.ViewHolder>() {

    var tag = "TopRatedAdapter"
    lateinit var binding: TopratedItemBinding

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        binding = TopratedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model = list[holder.adapterPosition]
        Glide.with(activity)
            .load("${BASE_IMAGE_URL}${model.poster_path}")
            .apply(RequestOptions.bitmapTransform(RoundedCorners(16)))
            .into(binding.posterPath)

        val setReleaseYear = changedateTimeFormat("yyyy-mm-dd","YYYY",model.release_date)

        binding.originalTitle.text = "${model.original_title}(${setReleaseYear})"
        binding.voteAverage.rating = (model.vote_average/2).toFloat()
        if(model.overview.isNotEmpty() && model.overview.length > 60) {
            addReadMore(model.overview, binding.overview)
        }
        else {
            binding.overview.text = model.overview
        }

        binding.cardView.setOnClickListener {

            onClick.openDetail(position,model.id)
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
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

    fun checkDataExist(movie: Result): Boolean {
        for(i in 0 until list.size) {
            val model = list.get(i)
            if(model.id == movie.id) {
                return true
            }
        }

        return false
    }

    fun add(movie: Result) {
        if(!checkDataExist(movie)) {
            list.add(movie)
            notifyItemInserted(list.size - 1)
        }
    }

    fun addAll(moveResults: List<Result>) {
        for (result in moveResults) {
            add(result)
        }
    }

    interface movieListOnClick {

        fun openDetail(pos: Int, movieId: Int)
    }
}