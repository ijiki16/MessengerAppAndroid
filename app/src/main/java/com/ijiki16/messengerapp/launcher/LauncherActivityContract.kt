package com.ijiki16.messengerapp.launcher

import com.ijiki16.messengerapp.launcher.model.LoginResult
import com.ijiki16.messengerapp.main.profile.model.UserInfo

interface LauncherActivityContract {

    interface Presenter {

        fun getUserInfo(userId: String)
        fun saveUserInfo(userId: String, username: String, about: String)

        fun saveUserInfoLocally(userInfo: UserInfo)
        fun onLogInResult(result: LoginResult)
    }

    interface View {
        fun loggedIn()
        fun showError(error: String)
    }
}