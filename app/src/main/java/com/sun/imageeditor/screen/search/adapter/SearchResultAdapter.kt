package com.sun.imageeditor.screen.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sun.imageeditor.R
import com.sun.imageeditor.data.model.Photo
import com.sun.imageeditor.data.model.PhotoCollection
import com.sun.imageeditor.databinding.ItemCollectionSearchResultBinding
import com.sun.imageeditor.databinding.ItemContainerPhotoBinding
import com.sun.imageeditor.utils.OnItemRecyclerViewClickListener
import com.sun.imageeditor.utils.SearchType
import com.sun.imageeditor.utils.ext.loadImageWithUrl


class SearchResultAdapter(
    private val searchType: SearchType
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mData by lazy { mutableListOf<Any>() }
    private var mOnItemClick: OnItemRecyclerViewClickListener<String>? = null

    var isPhotoLoading: Boolean = true

    fun setOnItemClick(onItemClick: OnItemRecyclerViewClickListener<String>?) {
        mOnItemClick = onItemClick
    }

    fun <T : Any> addData(list: MutableList<T>) {
        val startPosition = mData.size
        mData.addAll(list)
        notifyItemRangeInserted(startPosition, list.size)
    }

    fun <T : Any> setData(list: MutableList<T>) {
        mData.clear()
        mData.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            TYPE_PHOTO -> PhotoViewHolder(
                ItemContainerPhotoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
            ))
            TYPE_COLLECTION -> CollectionViewHolder(
                ItemCollectionSearchResultBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
            ))
            else -> throw IllegalStateException(ILLEGAL_VIEW_TYPE_ERROR)
        }
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is PhotoViewHolder -> {
                (mData[position] as? Photo)?.let { holder.bind(it) }
            }
            is CollectionViewHolder -> {
                (mData[position] as? PhotoCollection)?.let { holder.bind(it) }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(searchType) {
            SearchType.PHOTO -> TYPE_PHOTO
            SearchType.COLLECTION -> TYPE_COLLECTION
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        when(holder) {
            is PhotoViewHolder -> { holder.onLoading() }
            is CollectionViewHolder -> { holder.onLoading() }
        }
    }

    inner class PhotoViewHolder(
        private val binding: ItemContainerPhotoBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Photo) {
            binding.imagePhoto.run {
                setOnClickListener {
                    mOnItemClick?.onItemClick(data.photoFullUrl)
                }
                isClickable = true
                loadImageWithUrl(data.photoThumbUrl, R.color.light_grey)
            }
        }

        fun onLoading() {
            binding.imagePhoto.isClickable = false
        }
    }

    inner class CollectionViewHolder(
        private val binding: ItemCollectionSearchResultBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: PhotoCollection) {
            binding.textCollectionName.text = data.title
            binding.imageCollection.run {
                setOnClickListener {
                    mOnItemClick?.onItemClick(data.id)
                }
                isClickable = true
                loadImageWithUrl(data.coverPhotoUrl, R.color.light_grey)
            }
        }

        fun onLoading() {
            binding.imageCollection.isClickable = false
        }
    }

    companion object {
        const val TYPE_PHOTO = 0
        const val TYPE_COLLECTION = 1

        const val ILLEGAL_VIEW_TYPE_ERROR = "ILLEGAL_VIEW_TYPE_ERROR"
    }
}
