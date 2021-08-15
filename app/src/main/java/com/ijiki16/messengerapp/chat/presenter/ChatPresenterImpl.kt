package com.ijiki16.messengerapp.chat.presenter

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ijiki16.messengerapp.chat.ChatContract
import com.ijiki16.messengerapp.chat.model.MessageModel
import com.ijiki16.messengerapp.main.home.presenter.HomePresenterImpl

class ChatPresenterImpl(private val view: ChatContract.View): ChatContract.Presenter {

    override fun loadChat(userId: String) {
        val database = Firebase.database
        val messagesReference = database.getReference(HomePresenterImpl.DB_MESSAGES)

        val myUserId = Firebase.auth.currentUser?.uid!!

        val childKey = if(userId > myUserId) "$myUserId|$userId" else "$userId|$myUserId"
        messagesReference.child(childKey)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value == null) {
                        view.chatLoaded(emptyList())
                    } else {
                        (snapshot.value as HashMap<*, *>)
                            .entries
                            .forEach { entry ->
                                entry
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    view.showError(error.message)
                }

            })
    }

    override fun sendMessage(message: MessageModel) {
        TODO("Not yet implemented")
    }
}