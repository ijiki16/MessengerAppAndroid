package com.ijiki16.messengerapp.main.profile.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UserInfo(
    val username: String,
    val about: String,
    @SerializedName("profile") val profilePictureUrl: String
)
