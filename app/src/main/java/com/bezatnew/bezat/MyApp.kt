package com.bezatnew.bezat

import android.app.Application

class MyApp: Application() {
    companion object {
        var MyApp:Application ?= null
    }

    override fun onCreate() {
        super.onCreate()
        MyApp = this
    }
}