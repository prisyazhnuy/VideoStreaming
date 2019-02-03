package com.prisyazhnuy.streaming.preferences

import com.prisyazhnuy.streaming.NPApp
import com.prisyazhnuy.streaming.utils.EMPTY_STRING

internal object PreferencesProvider {

    private val preferences = NPApp.securePrefs

    var token: String
        get() = preferences.getString(PreferencesContract.TOKEN, EMPTY_STRING)
        set(value) {
            preferences.edit()
                    .putString(PreferencesContract.TOKEN, value)
                    .commit()
        }

    var refreshToken: String
        get() = preferences.getString(PreferencesContract.REFRESH_TOKEN, EMPTY_STRING)
        set(value) {
            preferences.edit()
                    .putString(PreferencesContract.REFRESH_TOKEN, value)
                    .commit()
        }

    fun saveSession(token: String, refreshToken: String) {
        preferences.edit()
                .putString(PreferencesContract.TOKEN, token)
                .putString(PreferencesContract.REFRESH_TOKEN, refreshToken)
                .commit()
    }

    fun clearData() {
        preferences.edit()
                .remove(PreferencesContract.TOKEN)
                .remove(PreferencesContract.REFRESH_TOKEN)
                .commit()
    }
}