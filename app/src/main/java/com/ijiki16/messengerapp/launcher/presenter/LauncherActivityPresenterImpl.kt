package com.ijiki16.messengerapp.launcher.presenter

import com.ijiki16.messengerapp.infrastructure.AppPreferences
import com.ijiki16.messengerapp.launcher.LauncherActivityContract
import com.ijiki16.messengerapp.launcher.model.LauncherActivityInteractor
import com.ijiki16.messengerapp.launcher.model.LoginResult
import com.ijiki16.messengerapp.main.profile.model.UserInfo

class LauncherActivityPresenterImpl(
    private val view: LauncherActivityContract.View,
    private val preferences: AppPreferences
    ): LauncherActivityContract.Presenter {


    private val interactor = LauncherActivityInteractor(this)

    override fun getUserInfo(userId: String) = interactor.getUserInfo(userId)

    override fun saveUserInfo(userId: String, username: String, about: String) =
        interactor.saveUserInfo(userId, username, about)

    override fun saveUserInfoLocally(userInfo: UserInfo) {
        preferences.saveUser(userInfo)
    }

    override fun onLogInResult(result: LoginResult) =
        if (result.loginStatus) {
            view.loggedIn()
        } else {
            view.showError(result.errorMsg)
        }

}