package com.ijiki16.messengerapp.launcher.model

import androidx.annotation.Keep

@Keep
data class LoginResult(
    val loginStatus: Boolean,
    val errorMsg: String
)
