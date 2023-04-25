package com.sun.imageeditor.screen.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sun.imageeditor.databinding.ItemRecentSearchBinding
import com.sun.imageeditor.utils.OnItemRecyclerViewClickListener

class RecentSearchAdapter : RecyclerView.Adapter<RecentSearchAdapter.RecentSearchViewHolder>() {
    private val mData by lazy { mutableListOf<String>() }
    private var mOnItemClick: OnItemRecyclerViewClickListener<Int>? = null

    var isLoading = true

    fun setData(list: List<String>) {
        mData.clear()
        mData.addAll(list)
        notifyDataSetChanged()
    }

    fun addData(list: List<String>) {
        val startPosition = mData.size
        mData.addAll(list)
        notifyItemRangeInserted(startPosition, list.size)
    }

    fun setOnItemClick(onItemClick: OnItemRecyclerViewClickListener<Int>) {
        mOnItemClick = onItemClick
    }

    fun getRecentSearch(index: Int): String {
        return mData.getOrNull(index) ?: ""
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentSearchViewHolder {
        return RecentSearchViewHolder(ItemRecentSearchBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: RecentSearchViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class RecentSearchViewHolder(
        private val binding: ItemRecentSearchBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(queryIndex: Int) {
            binding.buttonRecentSearch.run {
                text = mData[queryIndex]
                setOnClickListener { mOnItemClick?.onItemClick(queryIndex) }
            }
        }
    }
}
