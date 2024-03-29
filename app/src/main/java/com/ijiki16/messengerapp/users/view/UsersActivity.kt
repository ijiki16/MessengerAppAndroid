package com.ijiki16.messengerapp.users.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.ijiki16.messengerapp.R
import com.ijiki16.messengerapp.chat.view.ChatActivity
import com.ijiki16.messengerapp.databinding.ActivityUsersBinding
import com.ijiki16.messengerapp.databinding.ItemContactListLoadingBinding
import com.ijiki16.messengerapp.databinding.ItemUserBinding
import com.ijiki16.messengerapp.infrastructure.GlideApp
import com.ijiki16.messengerapp.users.UsersContract
import com.ijiki16.messengerapp.users.model.UserItemEntity
import com.ijiki16.messengerapp.users.presenter.UsersPresenterImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

class UsersActivity : UsersContract.View, AppCompatActivity() {

    private lateinit var  binding: ActivityUsersBinding
    private lateinit var presenter: UsersContract.Presenter
    private val adapter = UsersListAdapter()

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter = UsersPresenterImpl(this)

        setUpViews()
        loadMore()
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    private fun setUpViews() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?

                if (!adapter.isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == adapter.data.size - 1) {
                        loadMore()
                    }
                }
            }
        })
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.backArrow.setOnClickListener {
            onBackPressed()
        }

        binding.searchView.setOnNewSearchRequestListener{
            loadMore(it, 0)
        }
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    private fun loadMore(searchTerm: String = binding.searchView.searchTerm, from: Int = adapter.data.size) {
        adapter.setLoading()
        presenter.loadMore(searchTerm, from)
    }

    override fun moreLoaded(data: List<UserItemEntity>) {
        adapter.appendData(data)
    }

    override fun dataLoaded(data: List<UserItemEntity>) {
        if (data.isEmpty()) {
            showNoDataScreen(true)
        } else {
            showNoDataScreen(false)
        }
        adapter.setData(data)
    }

    private fun showNoDataScreen(show: Boolean) {
        binding.noDataTv.visibility = if(show) View.VISIBLE else View.GONE
    }

    override fun showError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    companion object {
        // https://stackoverflow.com/questions/34819334/kotlin-factory-on-inner-nested-class
        // inner classes can't have companion objects.
        private const val LIST_ITEM = 0
        private const val LOADING_ITEM = 1
    }

    inner class UsersListAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        val data = mutableListOf<UserItemEntity>()

        private var _isLoading = false
        val isLoading: Boolean = _isLoading

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return if (viewType == LIST_ITEM) {
                UsersViewHolder(ItemUserBinding.inflate(inflater, parent, false))
            } else {
                UsersLoadingItemViewHolder(ItemContactListLoadingBinding.inflate(inflater, parent, false))
            }
        }

        override fun getItemViewType(position: Int): Int =
            if (position == data.size) {
                LOADING_ITEM
            } else {
                LIST_ITEM
            }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (position < data.size) {
                holder as UsersViewHolder
                holder.setData(data[position])
            }
        }

        override fun getItemCount(): Int =
            if(_isLoading) data.size + 1 else data.size

        fun setLoading() {
            if(!_isLoading) {
                _isLoading = true
                notifyDataSetChanged()
            }
        }

        fun setData(newData: List<UserItemEntity>) {
            data.clear()
            appendData(newData)
        }

        fun appendData(newData: List<UserItemEntity>) {
            _isLoading = false
            data.addAll(newData)
            notifyDataSetChanged()
        }

    }

    inner class UsersViewHolder(
        private val binding: ItemUserBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun setData(data: UserItemEntity) {
            val storageReference = Firebase.storage.reference.child(data.profileUrl)
            GlideApp.with(this@UsersActivity)
                .load(storageReference)
                .placeholder(R.drawable.ic_baseline_account_circle_96)
                .error(R.drawable.ic_baseline_cancel_96)
                .into(binding.userAvatarIv)

            binding.userNicknameTv.text = data.username
            binding.aboutTv.text = data.about

            binding.root.setOnClickListener {
                openMessagingActivity(data.userId, data.profileUrl, data.username, data.about)
            }
        }

        private fun openMessagingActivity(userId: String, profilePic: String, username: String, about: String) {
            ChatActivity.startChatActivity(this@UsersActivity, userId, profilePic, username, about)
        }

    }

    inner class UsersLoadingItemViewHolder(
        binding: ItemContactListLoadingBinding
    ): RecyclerView.ViewHolder(binding.root)
}