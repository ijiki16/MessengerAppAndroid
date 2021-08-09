package com.ijiki16.messengerapp.infrastructure

import android.content.SharedPreferences
import com.ijiki16.messengerapp.main.profile.model.UserInfo

class AppPreferences(private val sharedPreferences: SharedPreferences) {

    fun saveUser(username: String, password: String) {
        with(sharedPreferences.edit()) {
            putString(PREF_USERNAME, username)
            putString(PREF_PASSWORD, password)
            apply()
        }
    }

    fun getUser(): UserInfo {
        val username = sharedPreferences.getString(PREF_USERNAME, "") ?: ""
        val password = sharedPreferences.getString(PREF_PASSWORD, "") ?: ""
        val about = sharedPreferences.getString(PREF_ABOUT, "") ?: ""
        val profileUrl = sharedPreferences.getString(PREF_PROFILE, "") ?: ""

        return UserInfo(profileUrl, username, password, about)
    }

    fun logout() {
        with(sharedPreferences.edit()) {
            putString(PREF_USERNAME, "")
            putString(PREF_PASSWORD, "")
            putString(PREF_ABOUT, "")
            putString(PREF_PROFILE, "")
            apply()
        }
    }

    companion object {
        const val PREF_USERNAME = "username"
        const val PREF_PASSWORD = "password"
        const val PREF_PROFILE = "profile"
        const val PREF_ABOUT = "about"
    }

}