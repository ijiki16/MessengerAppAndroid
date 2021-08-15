package com.ijiki16.messengerapp.chat.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ijiki16.messengerapp.chat.ChatContract
import com.ijiki16.messengerapp.chat.model.MessageModel
import com.ijiki16.messengerapp.chat.presenter.ChatPresenterImpl
import com.ijiki16.messengerapp.databinding.*
import com.ijiki16.messengerapp.main.home.view.HomeFragment

class ChatActivity : ChatContract.View, AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding

    private val userId: String by lazy { intent.getStringExtra(USER_ID)!! }
    private val userProfilePic: String by lazy { intent.getStringExtra(USER_PROFILE)!! }
    private val userNickname: String by lazy { intent.getStringExtra(USER_NICKNAME)!! }

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
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // TODO: set userProfilePic and userNickname
    }

    override fun chatLoaded(data: List<MessageModel>) {
        adapter.setData(data)
    }

    override fun showError(error: String) {
        // TODO: show error
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
            // TODO: not yet implemented
        }

    }

    inner class ChatViewHolderRight(
        private val binding: ItemChatRightBinding
    ) : ChatView, RecyclerView.ViewHolder(binding.root) {

        override fun setData(data: MessageModel) {
            // TODO: not yet implemented
        }

    }

    interface ChatView {
        fun setData(data: MessageModel)
    }

    companion object {
        private const val USER_ID = "user_id"
        private const val USER_PROFILE = "user_profile"
        private const val USER_NICKNAME = "user_nickname"

        private const val ITEM_LEFT = 0
        private const val ITEM_RIGHT = 1

        @JvmStatic
        fun startChatActivity(
            context: Context,
            userId: String,
            userProfilePic: String,
            userNickname: String
        ) {
            val myIntent = Intent(context, ChatActivity::class.java)
            myIntent.putExtra(USER_ID, userId)
            myIntent.putExtra(USER_PROFILE, userProfilePic)
            myIntent.putExtra(USER_NICKNAME, userNickname)
            context.startActivity(myIntent)
        }
    }

}