package com.sun.imageeditor.screen.edit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sun.imageeditor.R
import com.sun.imageeditor.databinding.ItemIconBinding
import com.sun.imageeditor.utils.OnItemRecyclerViewClickListener

class IconAdapter : RecyclerView.Adapter<IconAdapter.IconViewHolder>() {
    private var selectedPosition = 0
    private val mIconList = mutableListOf<Int>()
    private var mOnItemClickListener: OnItemRecyclerViewClickListener<Int>? = null

    fun setIconList(list: List<Int>) {
        mIconList.clear()
        mIconList.addAll(list)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClick: OnItemRecyclerViewClickListener<Int>) {
        mOnItemClickListener = onItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconViewHolder {
        return IconViewHolder(
            ItemIconBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun getItemCount(): Int {
        return mIconList.size
    }

    override fun onBindViewHolder(holder: IconViewHolder, position: Int) {
        holder.bind(mIconList[position])
    }

    inner class IconViewHolder(
        private val binding: ItemIconBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.buttonIcon.setOnClickListener {
                val position = adapterPosition

                val previousPosition = selectedPosition

                if (position != previousPosition) {
                    selectedPosition = position
                    notifyItemChanged(position)
                    notifyItemChanged(previousPosition)

                    mOnItemClickListener?.onItemClick(mIconList[position])
                }
            }
        }

        fun bind(item: Int) {
            binding.buttonIcon.setImageResource(item)

            if (adapterPosition == selectedPosition) {
                binding.buttonIcon.background = ContextCompat.getDrawable(
                    binding.root.context, R.drawable.shape_edit_activity_button
                )
            } else {
                binding.buttonIcon.background = null
            }
        }
    }
}
