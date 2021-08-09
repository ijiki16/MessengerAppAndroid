package com.ijiki16.messengerapp.main.profile.presenter

import com.ijiki16.messengerapp.infrastructure.AppPreferences
import com.ijiki16.messengerapp.main.profile.ProfileContract

class ProfilePresenterImpl(
    private val view: ProfileContract.View,
    private val preferences: AppPreferences
): ProfileContract.Presenter {

    override fun retrieveUserInfo() {
        val user = preferences.getUser()
        view.userInfoRetrieved(user)
    }

    override fun updateProfile(username: String, about: String) {
        // TODO: call service here to update profile information
//        view.profileUpdated()
    }

    override fun logout() {
        preferences.logout()
        view.loggedOut()
    }

}