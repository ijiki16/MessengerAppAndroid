package com.ijiki16.messengerapp.chat.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.ijiki16.messengerapp.R
import com.ijiki16.messengerapp.chat.ChatContract
import com.ijiki16.messengerapp.chat.model.MessageModel
import com.ijiki16.messengerapp.chat.presenter.ChatPresenterImpl
import com.ijiki16.messengerapp.databinding.*
import com.ijiki16.messengerapp.infrastructure.GlideApp
import com.ijiki16.messengerapp.infrastructure.toHumanReadableDate

class ChatActivity : ChatContract.View, AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding

    private val userId: String by lazy { intent.getStringExtra(USER_ID)!! }
    private val userProfilePic: String by lazy { intent.getStringExtra(USER_PROFILE)!! }
    private val userNickname: String by lazy { intent.getStringExtra(USER_NICKNAME)!! }
    private val userAbout: String by lazy { intent.getStringExtra(USER_ABOUT)!! }

    private lateinit var presenter: ChatContract.Presenter
    private val adapter = ChatAdapter()

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
        binding.recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, true)
        binding.recyclerView.adapter = adapter

        binding.userAbout.text = userAbout
        binding.userName.text = userNickname

        binding.backButton.setOnClickListener { onBackPressed() }

        val storageReference = Firebase.storage.reference.child(userProfilePic)
        GlideApp.with(this)
            .load(storageReference)
            .placeholder(R.drawable.ic_baseline_account_circle_96)
            .error(R.drawable.ic_baseline_cancel_96)
            .into(binding.userAvatar)

        binding.sendIv.setOnClickListener {
            presenter.sendMessage(
                userId,
                MessageModel(
                    true,
                    binding.messageEt.text.toString(),
                    System.currentTimeMillis()
                )
            )
            binding.messageEt.setText("")
        }

    }

    override fun chatLoaded(data: List<MessageModel>) {
        adapter.setData(data.sortedByDescending { it.sendDateTimestamp })
    }

    override fun showError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    inner class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private var data = mutableListOf<MessageModel>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return if (viewType == ITEM_LEFT) {
                ChatViewHolderLeft(ItemChatLeftBinding.inflate(inflater, parent, false))
            } else {
                ChatViewHolderRight(ItemChatRightBinding.inflate(inflater, parent, false))
            }
        }

        override fun getItemViewType(position: Int): Int =
            if (data[position].sentByMe) ITEM_RIGHT else ITEM_LEFT

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as ChatView).setData(data[position])
        }

        override fun getItemCount(): Int = data.size

        fun setData(newData: List<MessageModel>) {
            data.clear()
            data.addAll(newData)
            notifyDataSetChanged()
        }

    }

    inner class ChatViewHolderLeft(
        private val binding: ItemChatLeftBinding
    ) : ChatView, RecyclerView.ViewHolder(binding.root) {

        override fun setData(data: MessageModel) {
            binding.messageTime.text = data.sendDateTimestamp.toHumanReadableDate()
            binding.messageView.text = data.text
        }

    }

    inner class ChatViewHolderRight(
        private val binding: ItemChatRightBinding
    ) : ChatView, RecyclerView.ViewHolder(binding.root) {

        override fun setData(data: MessageModel) {
            binding.messageTime.text = data.sendDateTimestamp.toHumanReadableDate()
            binding.messageView.text = data.text
        }

    }

    interface ChatView {
        fun setData(data: MessageModel)
    }

    companion object {
        private const val USER_ID = "user_id"
        private const val USER_PROFILE = "user_profile"
        private const val USER_NICKNAME = "user_nickname"
        private const val USER_ABOUT = "user_about"

        private const val ITEM_LEFT = 0
        private const val ITEM_RIGHT = 1

        @JvmStatic
        fun startChatActivity(
            context: Context,
            userId: String,
            userProfilePic: String,
            userNickname: String,
            userAbout: String
        ) {
            val myIntent = Intent(context, ChatActivity::class.java)
            myIntent.putExtra(USER_ID, userId)
            myIntent.putExtra(USER_PROFILE, userProfilePic)
            myIntent.putExtra(USER_NICKNAME, userNickname)
            myIntent.putExtra(USER_ABOUT, userAbout)
            context.startActivity(myIntent)
        }
    }

}