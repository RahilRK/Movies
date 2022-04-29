package com.rk.movies.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.rk.movies.R
import com.rk.movies.util.Application
import com.rk.movies.util.GlobalClass
import kotlinx.android.synthetic.main.activity_main.*

class SecondActivity : AppCompatActivity() {

    private var tag = "SecondActivity"

    lateinit var navController: NavController

    lateinit var globalClass: GlobalClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        navController = findNavController(R.id.fragment)

        init()
        onClick()
    }

    fun init() {
        globalClass = (application as Application).globalClass
    }

    fun onClick() {

        navController.addOnDestinationChangedListener { controller, destination, arguments ->

            Log.d(tag, "onDestinationChanged: "+destination.label)
        }
    }
}