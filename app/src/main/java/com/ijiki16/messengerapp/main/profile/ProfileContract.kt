package com.ijiki16.messengerapp.main.profile

import android.net.Uri
import com.ijiki16.messengerapp.main.profile.model.UserInfo

interface ProfileContract {

    interface Presenter {
        fun retrieveUserInfo()
        fun updateProfile(username: String, about: String)
        fun updateImageUrl(imageUrl: String, imageUri: Uri)
        fun logout()
    }

    interface View {
        fun showError(error: String)
        fun userInfoRetrieved(user: UserInfo)
        fun profileUpdated(user: UserInfo)
        fun profileImageUpdated(data: Uri)
        fun loggedOut()
    }

}