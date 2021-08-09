package com.ijiki16.messengerapp.infrastructure

import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.ijiki16.messengerapp.main.profile.model.UserInfo

class AppPreferences {

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
        val profileUrl = sharedPreferences.getString(PREF_PROFILE, DEFAULT_PROFILE_URL) ?: DEFAULT_PROFILE_URL

        return UserInfo(profileUrl, username, password, about)
    }

    fun logout(): Boolean {
        with(sharedPreferences.edit()) {
            putString(PREF_USERNAME, "")
            putString(PREF_PASSWORD, "")
            putString(PREF_ABOUT, "")
            putString(PREF_PROFILE, DEFAULT_PROFILE_URL)
            return commit()
        }
    }

    companion object {

        private lateinit var sharedPreferences: SharedPreferences
        private var instance: AppPreferences? = null

        @JvmStatic
        fun getInstance(pref: SharedPreferences): AppPreferences {
            if (instance == null) {
                instance = AppPreferences()
                sharedPreferences = pref
            }
            return instance!!
        }

        private const val DEFAULT_PROFILE_URL = "public/avatar_image_placeholder.png"

        const val PREF_USERNAME = "username"
        const val PREF_PASSWORD = "password"
        const val PREF_PROFILE = "profile"
        const val PREF_ABOUT = "about"
    }

}