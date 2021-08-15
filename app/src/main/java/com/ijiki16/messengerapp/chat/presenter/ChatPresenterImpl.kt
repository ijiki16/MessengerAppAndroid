package com.ijiki16.messengerapp.chat.presenter

import com.ijiki16.messengerapp.chat.ChatContract
import com.ijiki16.messengerapp.chat.model.MessageModel

class ChatPresenterImpl(private val view: ChatContract.View): ChatContract.Presenter {
    override fun loadChat(userId: String) {
        TODO("Not yet implemented")
    }

    override fun sendMessage(message: MessageModel) {
        TODO("Not yet implemented")
    }
}