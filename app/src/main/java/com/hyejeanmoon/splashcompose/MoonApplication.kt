package com.hyejeanmoon.splashcompose

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MoonApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        EnvParameters.UNSPLASH_CLIENT_ID = getString(R.string.UNSPLASH_CLIENT_ID)
    }
}