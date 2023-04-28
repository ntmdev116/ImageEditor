package com.sun.imageeditor.screen.edit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sun.imageeditor.R
import com.sun.imageeditor.databinding.ItemFilterTypeBinding
import com.sun.imageeditor.utils.FilterType
import com.sun.imageeditor.utils.OnItemRecyclerViewClickListener

class FilterAdapter : RecyclerView.Adapter<FilterAdapter.FilterTypeViewHolder>() {
    private var selectedPosition = 0
    private val mFilterList = mutableListOf<FilterType>()
    private var mOnItemClickListener: OnItemRecyclerViewClickListener<FilterType>? = null

    fun setFilterList(list: List<FilterType>) {
        mFilterList.clear()
        mFilterList.addAll(list)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClick: OnItemRecyclerViewClickListener<FilterType>) {
        mOnItemClickListener = onItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterTypeViewHolder {
        return FilterTypeViewHolder(ItemFilterTypeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun getItemCount(): Int {
        return mFilterList.size
    }

    override fun onBindViewHolder(holder: FilterTypeViewHolder, position: Int) {
        holder.bind(mFilterList[position])
    }

    inner class FilterTypeViewHolder(
        private val binding: ItemFilterTypeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                val position = adapterPosition

                val previousPosition = selectedPosition
                selectedPosition = position
                notifyItemChanged(position)
                notifyItemChanged(previousPosition)

                mOnItemClickListener?.onItemClick(mFilterList[position])
            }
        }

        fun bind(item: FilterType) {
            binding.textFilterName.text = item.displayName

            if (adapterPosition == selectedPosition) {
                binding.textFilterName.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.selected_text)
                )
            } else {
                binding.textFilterName.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.default_text)
                )
            }
        }
    }
}
