package com.ijiki16.messengerapp.main.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ijiki16.messengerapp.R
import com.ijiki16.messengerapp.databinding.FragmentHomeBinding
import com.ijiki16.messengerapp.databinding.ItemContactListBinding
import com.ijiki16.messengerapp.databinding.ItemContactListLoadingBinding
import com.ijiki16.messengerapp.infrastructure.toHumanReadableDate
import com.ijiki16.messengerapp.main.home.HomeContract
import com.ijiki16.messengerapp.main.home.model.HomeMessageEntity
import com.ijiki16.messengerapp.main.home.presenter.HomePresenterImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


class HomeFragment : HomeContract.View, Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var presenter: HomeContract.Presenter
    private var adapter = ContactListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        presenter = HomePresenterImpl(this)
        return binding.root
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        loadMore()
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    private fun setupViews() {
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
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

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

    override fun showError(error: String) {
        // TODO: show error here
    }

    override fun moreLoaded(data: List<HomeMessageEntity>) {
        adapter.setData(data)
    }

    companion object {
        // https://stackoverflow.com/questions/34819334/kotlin-factory-on-inner-nested-class
        // inner classes can't have companion objects.
        private const val LIST_ITEM = 0
        private const val LOADING_ITEM = 1

        @JvmStatic
        fun newInstance() =
            HomeFragment()
    }


    inner class ContactListAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        val data = mutableListOf<HomeMessageEntity>()

        private var _isLoading = false
        val isLoading: Boolean = _isLoading

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return if (viewType == LIST_ITEM) {
                ContactListViewHolder(ItemContactListBinding.inflate(inflater, parent, false))
            } else {
                ContactsLoadingItemViewHolder(ItemContactListLoadingBinding.inflate(inflater, parent, false))
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
                holder as ContactListViewHolder
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

        fun setData(newData: List<HomeMessageEntity>) {
            _isLoading = false
            data.addAll(newData)
            data.sortBy { 0 - it.lastMessageDateTimestamp }
            notifyDataSetChanged()
        }

    }

    inner class ContactListViewHolder(
        private val binding: ItemContactListBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun setData(data: HomeMessageEntity) {
            Glide.with(requireContext())
                .load(data.userProfileUrl)
                .placeholder(R.drawable.ic_baseline_account_circle_96)
                .error(R.drawable.ic_baseline_cancel_96)
                .into(binding.userAvatarIv)

            binding.userNicknameTv.text = data.userNickname
            binding.lastMessageTv.text = data.lastMessage
            binding.lastMessageTimeTv.text = data.lastMessageDateTimestamp.toHumanReadableDate()
        }

    }

    inner class ContactsLoadingItemViewHolder(
        binding: ItemContactListLoadingBinding
    ): RecyclerView.ViewHolder(binding.root)
}