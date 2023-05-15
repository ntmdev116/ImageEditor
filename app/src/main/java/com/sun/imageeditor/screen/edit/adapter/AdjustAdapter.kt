package com.sun.imageeditor.screen.edit.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sun.imageeditor.R
import com.sun.imageeditor.databinding.ItemAdjustTypeBinding
import com.sun.imageeditor.utils.AdjustType
import com.sun.imageeditor.utils.OnItemRecyclerViewClickListener

class AdjustAdapter : RecyclerView.Adapter<AdjustAdapter.AdjustTypeViewHolder>() {
    private var selectedPosition = 0
    private val mAdjustList = mutableListOf<AdjustType>()
    private var mOnItemClickListener: OnItemRecyclerViewClickListener<AdjustType>? = null

    fun setAdjustList(list: List<AdjustType>) {
        mAdjustList.clear()
        mAdjustList.addAll(list)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClick: OnItemRecyclerViewClickListener<AdjustType>) {
        mOnItemClickListener = onItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdjustTypeViewHolder {
        return AdjustTypeViewHolder(
            ItemAdjustTypeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun getItemCount(): Int {
        return mAdjustList.size
    }

    override fun onBindViewHolder(holder: AdjustTypeViewHolder, position: Int) {
        holder.bind(mAdjustList[position])
    }

    inner class AdjustTypeViewHolder(
        private val binding: ItemAdjustTypeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.textAdjustType.setOnClickListener {
                val position = adapterPosition

                val previousPosition = selectedPosition

                if (position != previousPosition) {
                    selectedPosition = position
                    notifyItemChanged(position)
                    notifyItemChanged(previousPosition)

                    mOnItemClickListener?.onItemClick(mAdjustList[position])
                }
            }
        }

        fun bind(item: AdjustType) {
            binding.textAdjustType.text = item.displayName

            if (adapterPosition == selectedPosition) {
                binding.textAdjustType.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.selected_text)
                )
                binding.textAdjustType.background = ContextCompat.getDrawable(
                    binding.root.context, R.drawable.shape_edit_activity_button
                )
            } else {
                binding.textAdjustType.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.default_text)
                )
                binding.textAdjustType.background = null
            }
        }
    }
}
