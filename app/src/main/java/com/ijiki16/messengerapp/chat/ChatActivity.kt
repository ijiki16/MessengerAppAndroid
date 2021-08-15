package com.ijiki16.messengerapp.chat

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ijiki16.messengerapp.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private val userId: String by lazy { intent.getStringExtra(USER_ID)!! }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textView.text = userId
    }

    companion object {
        private const val USER_ID = "user_id"

        @JvmStatic
        fun startChatActivity(context: Context, userId: String) {
            val myIntent = Intent(context, ChatActivity::class.java)
            myIntent.putExtra(USER_ID, userId)
            context.startActivity(myIntent)
        }
    }

}