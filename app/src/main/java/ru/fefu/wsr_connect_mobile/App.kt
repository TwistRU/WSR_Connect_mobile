package ru.fefu.wsr_connect_mobile

import android.app.Application
import android.content.SharedPreferences

class App : Application() {

    companion object {
        lateinit var INSTANCE: App
        lateinit var sharedPreferences: SharedPreferences
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
    }
}