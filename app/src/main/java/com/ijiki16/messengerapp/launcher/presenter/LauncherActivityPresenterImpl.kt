package com.ijiki16.messengerapp.launcher.presenter

import android.content.SharedPreferences
import com.ijiki16.messengerapp.launcher.LauncherActivityContract
import com.ijiki16.messengerapp.launcher.model.LauncherActivityInteractor
import com.ijiki16.messengerapp.launcher.model.LoginResult
import java.security.MessageDigest

class LauncherActivityPresenterImpl(
    private val view: LauncherActivityContract.View,
    private val preferences: SharedPreferences
    ): LauncherActivityContract.Presenter {

    companion object {
        const val PSWD_HASH_ALGO = "SHA-256"
        const val PREF_USERNAME = "username"
        const val PREF_PASSWORD = "password"
    }

    private val interactor = LauncherActivityInteractor(this)

    override fun registerUser(username: String, password: String, about: String) =
        interactor.register(username, hashStr(password), about)

    override fun logInUser(username: String, password: String) =
        interactor.logIn(username, hashStr(password))

    override fun loginWithSavedUser(): Boolean {
        val username = preferences.getString(PREF_USERNAME, "")
        val password = preferences.getString(PREF_PASSWORD, "")
        return if (username.isNullOrBlank() || password.isNullOrBlank()) {
            false
        } else {
            interactor.logIn(username, password)
            true
        }
    }

    override fun onLogInResult(result: LoginResult) =
        if (result.loginStatus) {
            view.loggedIn()
        } else {
            view.showError(result.errorMsg)
        }

    override fun saveUserLocally(username: String, password: String) {
        with(preferences.edit()) {
            putString(PREF_USERNAME, username)
            putString(PREF_PASSWORD, password)
            apply()
        }
    }

    private fun hashStr(inStr: String) : String {
        val messageDigest = MessageDigest.getInstance(PSWD_HASH_ALGO)
        messageDigest.update(inStr.toByteArray())
        return String(messageDigest.digest())
    }
}