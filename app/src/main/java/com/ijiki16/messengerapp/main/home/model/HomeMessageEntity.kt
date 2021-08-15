package com.ijiki16.messengerapp.main.home.model

import androidx.annotation.Keep

@Keep
data class HomeMessageEntity(
    val userId: String,
    val lastMessage: String,
    val userProfileUrl: String?,
    val lastMessageDateTimestamp: Long,
    val userNickname: String?
)
