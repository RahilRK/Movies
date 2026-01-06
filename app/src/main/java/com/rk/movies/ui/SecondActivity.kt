package com.rk.movies.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.rk.movies.R
import com.rk.movies.util.GlobalClass
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SecondActivity : AppCompatActivity() {

    private var tag = "SecondActivity"

    lateinit var navController: NavController

    @Inject
    lateinit var globalClass: GlobalClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        navController = findNavController(R.id.fragment)

        onClick()
    }

    fun onClick() {

        navController.addOnDestinationChangedListener { controller, destination, arguments ->

            Log.d(tag, "onDestinationChanged: "+destination.label)
        }
    }
}