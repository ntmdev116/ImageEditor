package com.sun.imageeditor.screen.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sun.imageeditor.R
import com.sun.imageeditor.data.model.PhotoCollection
import com.sun.imageeditor.databinding.ItemContainerCollecitonCoverPhotoBinding
import com.sun.imageeditor.utils.OnItemRecyclerViewClickListener

class CollectionCoverPhotoAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mCoverPhotos = mutableListOf<PhotoCollection>()
    private var mIsLoading = true
    private var mOnItemClick: OnItemRecyclerViewClickListener<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CoverPhotoViewHolder(ItemContainerCollecitonCoverPhotoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun getItemCount(): Int = mCoverPhotos.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CoverPhotoViewHolder -> holder.bind(mCoverPhotos[position])
            // TODO: implement other view holder types
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if (mIsLoading && holder is CoverPhotoViewHolder) holder.onLoading()
    }

    fun setCoverPhotos(list: MutableList<PhotoCollection>) {
        mCoverPhotos.clear()
        mCoverPhotos.addAll(list)
        notifyDataSetChanged()
    }

    fun addCoverPhotos(list: MutableList<PhotoCollection>) {
        val startPosition = mCoverPhotos.size
        mCoverPhotos.addAll(list)
        notifyItemRangeInserted(startPosition, list.size)
    }
    inner class CoverPhotoViewHolder(private val binding: ItemContainerCollecitonCoverPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PhotoCollection) {
            binding.imageCoverPhoto.let {
                it.setOnClickListener {
                    mOnItemClick?.onItemClick(item.id)
                }
                it.isClickable = true
            }

            // TODO: get bitmap from url then load with Glide
        }

        fun onLoading() = with (binding) {
            imageCoverPhoto.setImageResource(R.color.light_grey)
            imageCoverPhoto.isClickable = false
            textCollectionTitle.text = ""
        }
    }
}
