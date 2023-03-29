package com.sun.imageeditor.screen.home.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sun.imageeditor.R
import com.sun.imageeditor.data.model.Photo
import com.sun.imageeditor.databinding.ItemContainerPhotoBinding
import com.sun.imageeditor.utils.OnItemRecyclerViewClickListener
import java.util.concurrent.ConcurrentHashMap

class PhotoAdapter : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {
    private val mPhotos = mutableListOf<Photo>()
    private var mOnItemClick: OnItemRecyclerViewClickListener<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(ItemContainerPhotoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun getItemCount(): Int = mPhotos.size

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(mPhotos[position])
    }

    override fun onViewRecycled(holder: PhotoViewHolder) {
        super.onViewRecycled(holder)
        holder.onLoading()
    }

    fun setCoverPhotos(list: MutableList<Photo>) {
        mPhotos.clear()
        mPhotos.addAll(list)
        notifyDataSetChanged()
    }

    fun addCoverPhotos(list: MutableList<Photo>) {
        val startPosition = mPhotos.size
        mPhotos.addAll(list)
        notifyItemRangeInserted(startPosition, list.size)
    }

    inner class PhotoViewHolder(private val binding: ItemContainerPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Photo) {
            binding.imagePhoto.let {
                it.setOnClickListener {
                    mOnItemClick?.onItemClick(item.id)
                }
                it.isClickable = true
            }

            // TODO: get bitmap from url then load with Glide
        }

        fun onLoading() {
            binding.imagePhoto.setImageResource(R.color.light_grey)
            binding.imagePhoto.isClickable = false
            binding.textPhotoTitle.text = ""
        }
    }
}
