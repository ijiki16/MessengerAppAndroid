package com.ijiki16.messengerapp.users.model

import androidx.annotation.Keep

@Keep
data class UserItemEntity(
    val userId: String,
    val username: String,
    val about: String,
    val profileUrl: String
)
