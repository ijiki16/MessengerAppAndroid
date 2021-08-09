package com.ijiki16.messengerapp.launcher.presenter

import com.ijiki16.messengerapp.infrastructure.AppPreferences
import com.ijiki16.messengerapp.launcher.LauncherActivityContract
import com.ijiki16.messengerapp.launcher.model.LauncherActivityInteractor
import com.ijiki16.messengerapp.launcher.model.LoginResult
import java.security.MessageDigest

class LauncherActivityPresenterImpl(
    private val view: LauncherActivityContract.View,
    private val preferences: AppPreferences
    ): LauncherActivityContract.Presenter {


    private val interactor = LauncherActivityInteractor(this)

    override fun registerUser(username: String, password: String, about: String) =
        interactor.register(username, hashStr(password), about)

    override fun logInUser(username: String, password: String) =
        interactor.logIn(username, hashStr(password))

    override fun loginWithSavedUser(): Boolean {
        val user = preferences.getUser()
        return if (user.username.isBlank() || user.password.isBlank()) {
            false
        } else {
            interactor.logIn(user.username, user.password)
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
        preferences.saveUser(username, password)
    }

    private fun hashStr(inStr: String) : String {
        val messageDigest = MessageDigest.getInstance(PSWD_HASH_ALGO)
        messageDigest.update(inStr.toByteArray())
        return String(messageDigest.digest())
    }

    companion object {
        const val PSWD_HASH_ALGO = "SHA-256"
    }

}