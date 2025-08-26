package com.example.mylearningproject

import android.app.Application
import android.content.Context

class SavedTextApplication : Application() {
    init {
        app = this
    }
    companion object{
        private lateinit var app: SavedTextApplication
        fun getAppContext() : Context = app.applicationContext
    }
}
