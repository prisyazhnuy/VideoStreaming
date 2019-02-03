package com.prisyazhnuy.streaming

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.cleveroad.bootstrap.kotlin_ext.safeLet
import com.facebook.stetho.Stetho
import com.prisyazhnuy.streaming.models.Session
import com.prisyazhnuy.streaming.models.SessionModel
import com.prisyazhnuy.streaming.preferences.PreferencesProvider
import com.securepreferences.SecurePreferences


// TODO rename application class
class NPApp : Application() {

    companion object {
        lateinit var instance: NPApp
            private set
        lateinit var securePrefs: SecurePreferences
    }

    private var currentSession: Session = SessionModel()

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        securePrefs = getSharedPreferences()
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }

    fun setSession(session: Session?) {
        session?.let { currentSession = it }
    }

    fun saveSession() {
        currentSession.run {
            safeLet(accessToken, refreshToken) { token, rfToken ->
                PreferencesProvider.saveSession(token, rfToken)
            }
        }
    }

    fun getSession() = currentSession

    fun onLogout() {
        // TODO need implemented logout
        PreferencesProvider.clearData()
    }

    private fun getSharedPreferences() =
            SecurePreferences(this,
                    BuildConfig.SECURE_PREF_PASSWORD,
                    BuildConfig.SECURE_PREF_NAME)
}
