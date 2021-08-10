package com.ijiki16.messengerapp.infrastructure

import android.content.SharedPreferences
import com.ijiki16.messengerapp.main.profile.model.UserInfo

class AppPreferences {

    fun saveUser(userInfo: UserInfo) {
        with(sharedPreferences.edit()) {
            putString(PREF_USERNAME, userInfo.username)
            putString(PREF_ABOUT, userInfo.about)
            putString(PREF_PROFILE, userInfo.profilePictureUrl)
            apply()
        }
    }

    fun updateUser(username: String, about: String) {
        with(sharedPreferences.edit()) {
            putString(PREF_USERNAME, username)
            putString(PREF_ABOUT, about)
            apply()
        }
    }

    fun getUser(): UserInfo {
        val username = sharedPreferences.getString(PREF_USERNAME, "") ?: ""
        val about = sharedPreferences.getString(PREF_ABOUT, "") ?: ""
        val profileUrl = sharedPreferences.getString(PREF_PROFILE, DEFAULT_PROFILE_URL) ?: DEFAULT_PROFILE_URL

        return UserInfo(username, about, profileUrl)
    }

    fun updateProfileUrl(url: String) {
        with(sharedPreferences.edit()) {
            putString(PREF_PROFILE, url)
            apply()
        }
    }

    fun logout(): Boolean {
        with(sharedPreferences.edit()) {
            putString(PREF_USERNAME, "")
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

        const val DEFAULT_PROFILE_URL = "public/avatar_image_placeholder.png"

        const val PREF_USERNAME = "username"
        const val PREF_PROFILE = "profile"
        const val PREF_ABOUT = "about"
    }

}