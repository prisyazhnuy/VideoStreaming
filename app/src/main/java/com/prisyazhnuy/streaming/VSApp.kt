package com.prisyazhnuy.streaming

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.facebook.stetho.Stetho
import com.securepreferences.SecurePreferences


class VSApp : Application() {

    companion object {
        lateinit var instance: VSApp
            private set
        lateinit var securePrefs: SecurePreferences
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }
}