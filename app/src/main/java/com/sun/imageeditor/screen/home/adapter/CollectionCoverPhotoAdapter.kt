package com.sun.imageeditor.screen.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sun.imageeditor.R
import com.sun.imageeditor.data.model.PhotoCollection
import com.sun.imageeditor.databinding.ItemContainerCollectionCoverPhotoBinding
import com.sun.imageeditor.utils.OnItemRecyclerViewClickListener
import com.sun.imageeditor.utils.ext.loadImageWithUrl

class CollectionCoverPhotoAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mCoverPhotos = mutableListOf<PhotoCollection>()
    private var mOnItemClick: OnItemRecyclerViewClickListener<String>? = null
    var isLoading = true
        set(value) {
            if (!value) notifyItemRemoved(mCoverPhotos.size)
            else notifyItemInserted(mCoverPhotos.size)
            field = value
        }

    fun setOnItemClick(onClick: OnItemRecyclerViewClickListener<String>) {
        mOnItemClick = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_VIEW_ITEM -> CoverPhotoViewHolder(ItemContainerCollectionCoverPhotoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ))
            TYPE_VIEW_LOADING -> LoadingViewHolder(ItemContainerCollectionCoverPhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ))
            else -> throw IllegalStateException(ILLEGAL_VIEW_TYPE_ERROR)
        }
    }

    override fun getItemCount(): Int = mCoverPhotos.size + if (isLoading) 1 else 0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CoverPhotoViewHolder -> holder.bind(mCoverPhotos[position])
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if (holder is CoverPhotoViewHolder) holder.onLoading()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < mCoverPhotos.size) TYPE_VIEW_ITEM else TYPE_VIEW_LOADING
    }

    fun setCoverPhotos(list: MutableList<PhotoCollection>) {
        mCoverPhotos.clear()
        mCoverPhotos.addAll(list)
        notifyDataSetChanged()
    }

    fun addCoverPhotos(list: MutableList<PhotoCollection>) {
        val startPosition = mCoverPhotos.size + 1
        mCoverPhotos.addAll(list)
        notifyItemRangeInserted(startPosition, list.size)
    }

    inner class CoverPhotoViewHolder(
        private val binding: ItemContainerCollectionCoverPhotoBinding
        ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PhotoCollection) {
            binding.imageCoverPhoto.let {
                it.setOnClickListener {
                    mOnItemClick?.onItemClick(item.id)
                }
                it.isClickable = true
                it.loadImageWithUrl(item.coverPhotoUrl, R.color.light_grey)
                it.setOnClickListener { mOnItemClick?.onItemClick(item.id) }
            }

            binding.textCollectionTitle.text = item.title
        }

        fun onLoading() = with (binding) {
            imageCoverPhoto.isClickable = false
            textCollectionTitle.text = ""
        }
    }

    inner class LoadingViewHolder(
        private val binding: ItemContainerCollectionCoverPhotoBinding
        ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.progressLoading.visibility = View.VISIBLE
            binding.textCollectionTitle.text = ""
        }
    }

    companion object {
        private const val TYPE_VIEW_ITEM = 0
        private const val TYPE_VIEW_LOADING = 1

        const val ILLEGAL_VIEW_TYPE_ERROR = "ILLEGAL_VIEW_TYPE_ERROR"
    }
}
