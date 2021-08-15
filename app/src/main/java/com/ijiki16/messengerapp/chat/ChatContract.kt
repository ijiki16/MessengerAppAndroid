package com.ijiki16.messengerapp.chat

import com.ijiki16.messengerapp.chat.model.MessageModel

interface ChatContract {

    interface Presenter {
        fun loadChat(userId: String)
        fun sendMessage(message: MessageModel)
    }

    interface View {
        fun showError(error: String)
        fun chatLoaded(data: List<MessageModel>)
    }

}