package com.ijiki16.messengerapp.main.profile.presenter

import android.net.Uri
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ijiki16.messengerapp.infrastructure.AppPreferences
import com.ijiki16.messengerapp.launcher.model.LauncherActivityInteractor
import com.ijiki16.messengerapp.main.profile.ProfileContract
import com.ijiki16.messengerapp.main.profile.model.UserInfo

class ProfilePresenterImpl(
    private val view: ProfileContract.View,
    private val preferences: AppPreferences
) : ProfileContract.Presenter {

    override fun retrieveUserInfo() {
        val user = preferences.getUser()
        view.userInfoRetrieved(user)
    }

    override fun updateImageUrl(imageUrl: String, imageUri: Uri) {
        val uid = Firebase.auth.currentUser?.uid ?: ""

        val database = Firebase.database
        val usersReference = database.getReference(DB_USERS)
        val userReference = usersReference.child(uid)

        userReference.child(DB_PROFILE_KEY).setValue(imageUrl).addOnSuccessListener {
            preferences.updateProfileUrl(imageUrl)
            view.profileImageUpdated(imageUri)
        }.addOnFailureListener {
            view.showError(it.message ?: "could not upload an image.")
        }
    }

    override fun updateProfile(username: String, about: String) {
        val uid = Firebase.auth.currentUser?.uid ?: ""

        val database = Firebase.database
        val usersReference = database.getReference(DB_USERS)
        val userReference = usersReference.child(uid)

        Firebase.auth.currentUser?.updateEmail("$username@messenger.app")

        userReference.child(DB_USERNAME).setValue(username)
            .continueWithTask {
                userReference.child(DB_ABOUT_KEY).setValue(about)
            }
            .addOnSuccessListener {
                preferences.updateUser(username, about)
                view.profileUpdated(
                    UserInfo(username, about, "")
                )
            }.addOnFailureListener {
                view.showError(it.message ?: "could not upload an image.")
            }
    }

    override fun logout() {
        preferences.logout()
        view.loggedOut()
    }

    companion object {
        const val DB_USERS = "users"
        const val DB_ABOUT_KEY = "about"
        const val DB_PROFILE_KEY = "profile"
        const val DB_USERNAME = "username"
    }

}