package com.ijiki16.messengerapp.main.home.model

data class HomeMessageEntity(
    val lastMessage: String,
    val userProfileUrl: String,
    val lastMessageDateTimestamp: Long,
    val userNickname: String
)
