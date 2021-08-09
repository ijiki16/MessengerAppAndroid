package com.ijiki16.messengerapp.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ijiki16.messengerapp.databinding.FragmentHomeBinding
import com.ijiki16.messengerapp.databinding.ItemContactListBinding
import com.ijiki16.messengerapp.databinding.ItemContactListLoadingBinding


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private var isLoading = true //TODO: move this to presenter
    private var adapter = ContactListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        loadMore()
    }

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
    }

    private fun loadMore() {
        // TODO: load more data
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
                ContactListViewHolder(ItemContactListBinding.inflate(inflater))
            } else {
                ContactsLoadingItemViewHolder(ItemContactListLoadingBinding.inflate(inflater))
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
            if(isLoading) data.size + 1 else data.size

        fun setLoading() {
            _isLoading = true
            notifyDataSetChanged()
        }

        fun setData(newData: List<HomeMessageEntity>) {
            _isLoading = false
            data.addAll(newData)
            data.sortBy { 0 - it.lastMessageDateTimestamp }
            notifyDataSetChanged()
        }

    }

    inner class ContactListViewHolder(
        binding: ItemContactListBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun setData(data: HomeMessageEntity) {
            // TODO: draw with this data
        }

    }

    inner class ContactsLoadingItemViewHolder(
        binding: ItemContactListLoadingBinding
    ): RecyclerView.ViewHolder(binding.root)
}