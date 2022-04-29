package com.rk.movies.util

import android.app.Application

class Application: Application() {

    lateinit var repository: Repository
    lateinit var globalClass: GlobalClass

    override fun onCreate() {
        super.onCreate()

        init()
    }

    fun init() {

        globalClass = GlobalClass.getInstance(applicationContext)
        repository = Repository(globalClass)
    }
}