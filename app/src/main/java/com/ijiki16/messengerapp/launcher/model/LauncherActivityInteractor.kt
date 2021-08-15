package com.ijiki16.messengerapp.launcher.model

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ijiki16.messengerapp.infrastructure.AppPreferences
import com.ijiki16.messengerapp.launcher.LauncherActivityContract
import com.ijiki16.messengerapp.main.profile.model.UserInfo

class LauncherActivityInteractor(private val presenter: LauncherActivityContract.Presenter) {

    companion object {
        const val DB_USERS = "users"
        const val DB_ABOUT_KEY = "about"
        const val DB_PROFILE_KEY = "profile"
        const val DB_USERNAME = "username"
    }

    fun getUserInfo(userId: String) {
        val database = Firebase.database
        val usersReference = database.getReference(DB_USERS)
        val userReference = usersReference.child(userId)

        userReference.get().addOnSuccessListener {
            val userInfo = (it.value as HashMap<*, *>)
            presenter.saveUserInfoLocally(
                UserInfo(
                    userInfo[DB_USERNAME].toString(),
                    userInfo[DB_ABOUT_KEY].toString(),
                    userInfo[DB_PROFILE_KEY].toString(),
                )
            )
            presenter.onLogInResult(LoginResult(true, ""))
        }.addOnFailureListener {
            presenter.onLogInResult(LoginResult(false, "Connection to database was lost."))
        }
    }

    fun saveUserInfo(userId: String, username: String, about: String) {
        val database = Firebase.database
        val usersReference = database.getReference(DB_USERS)
        val userReference = usersReference.child(userId)

        userReference.child(DB_ABOUT_KEY).setValue(about)
            .continueWithTask {
                userReference.child(DB_PROFILE_KEY).setValue(AppPreferences.DEFAULT_PROFILE_URL)
            }.continueWithTask {
                userReference.child(DB_USERNAME).setValue(username)
            }.addOnSuccessListener {
                presenter.saveUserInfoLocally(
                    UserInfo(
                        username,
                        about,
                        AppPreferences.DEFAULT_PROFILE_URL,
                    )
                )
                presenter.onLogInResult(LoginResult(true, ""))
            }.addOnFailureListener {
                presenter.onLogInResult(LoginResult(false, it.message ?: "Something went wrong saving user info."))
            }
    }

}