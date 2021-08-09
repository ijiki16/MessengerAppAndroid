package com.ijiki16.messengerapp.main.home

data class HomeMessageEntity(
    val lastMessage: String,
    val userProfileUrl: String,
    val lastMessageDateTimestamp: Long
)
