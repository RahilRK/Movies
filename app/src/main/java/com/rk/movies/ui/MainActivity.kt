package com.rk.movies.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.rk.movies.R
import com.rk.movies.util.Application
import com.rk.movies.util.GlobalClass
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var tag = "MainActivity"

    lateinit var navController: NavController

    lateinit var globalClass: GlobalClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.fragment)
        bottomNavigationView.setupWithNavController(navController)

        init()
        onClick()
    }

    fun init() {
        globalClass = (application as Application).globalClass
    }

    fun onClick() {

        navController.addOnDestinationChangedListener { controller, destination, arguments ->

            Log.d(tag, "onDestinationChanged: "+destination.label)
            show_hideHomeViews(destination.label)
        }
    }

    fun show_hideHomeViews(label: CharSequence?) {

        if(label == resources.getString(R.string.now_playing) ||
            label == resources.getString(R.string.top_rated)) {

            bottomNavigationView.visibility = View.VISIBLE
            slideUpBottomNav()
        }
        else if(label == resources.getString(R.string.movie_detail)) {

            bottomNavigationView.visibility = View.GONE
            slideDownBottomNav()
        }
    }

    fun slideDownBottomNav() {

        bottomNavigationView.clearAnimation();
        bottomNavigationView
            .animate()
            .translationY(bottomNavigationView.height.toFloat())
            .setDuration(500);
    }

    fun slideUpBottomNav() {
        
        bottomNavigationView.clearAnimation();
        bottomNavigationView
            .animate()
            .translationY(0F)
            .setDuration(500);
    }
}