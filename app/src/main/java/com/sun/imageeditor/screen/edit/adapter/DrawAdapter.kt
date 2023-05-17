package com.sun.imageeditor.screen.edit.adapter

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sun.imageeditor.R
import com.sun.imageeditor.databinding.ItemIconBinding
import com.sun.imageeditor.utils.OnItemRecyclerViewClickListener

class DrawAdapter : RecyclerView.Adapter<DrawAdapter.ColorViewHolder>() {
    private var selectedPosition = 0
    private val mColorList = mutableListOf<Int>()
    private var mOnItemClickListener: OnItemRecyclerViewClickListener<Int>? = null

    fun setColorList(list: List<Int>) {
        mColorList.clear()
        mColorList.addAll(list)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClick: OnItemRecyclerViewClickListener<Int>) {
        mOnItemClickListener = onItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        return ColorViewHolder(
            ItemIconBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ))
    }

    override fun getItemCount(): Int {
        return mColorList.size
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.bind(mColorList[position])
    }

    inner class ColorViewHolder(
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

                    mOnItemClickListener?.onItemClick(mColorList[position])
                }
            }
        }

        fun bind(item: Int) {
            val gradientDrawable = ContextCompat
                .getDrawable(itemView.context, R.drawable.circle_with_border_shape) as? GradientDrawable

            gradientDrawable?.setColor(item)
            binding.buttonIcon.setImageDrawable(gradientDrawable)

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
