package com.ijiki16.messengerapp.launcher

import com.ijiki16.messengerapp.launcher.model.LoginResult

interface LauncherActivityContract {

    interface Presenter {
        fun registerUser(username: String, password: String, about: String)
        fun logInUser(username: String, password: String)
        fun loginWithSavedUser(): Boolean

        fun onLogInResult(result: LoginResult)
        fun saveUserLocally(username: String, password: String)
    }

    interface View {
        fun loggedIn()
        fun showError(error: String)
    }
}