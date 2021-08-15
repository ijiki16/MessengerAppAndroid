package com.ijiki16.messengerapp.chat.model

import androidx.annotation.Keep

@Keep
data class MessageModel(
    var sentByMe: Boolean,
    var text: String?,
    var sendDateTimestamp: Long,
)
