package com.ijiki16.messengerapp.chat.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ijiki16.messengerapp.chat.ChatContract
import com.ijiki16.messengerapp.chat.model.MessageModel
import com.ijiki16.messengerapp.chat.presenter.ChatPresenterImpl
import com.ijiki16.messengerapp.databinding.ActivityChatBinding

class ChatActivity : ChatContract.View, AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private val userId: String by lazy { intent.getStringExtra(USER_ID)!! }
    private val userProfilePic: String by lazy { intent.getStringExtra(USER_PROFILE)!! }
    private val userNickname: String by lazy { intent.getStringExtra(USER_NICKNAME)!! }
    private lateinit var presenter: ChatContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = ChatPresenterImpl(this)

        setUpViews()
        loadData()
    }

    private fun loadData() {
        presenter.loadChat(userId)
    }

    private fun setUpViews() {

    }

    override fun chatLoaded(data: List<MessageModel>) {
        TODO("Not yet implemented")
    }

    override fun showError(error: String) {
        // TODO: show error
    }

    companion object {
        private const val USER_ID = "user_id"
        private const val USER_PROFILE = "user_profile"
        private const val USER_NICKNAME = "user_nickname"

        @JvmStatic
        fun startChatActivity(context: Context, userId: String, userProfilePic: String, userNickname: String) {
            val myIntent = Intent(context, ChatActivity::class.java)
            myIntent.putExtra(USER_ID, userId)
            context.startActivity(myIntent)
        }
    }

}