package com.sun.imageeditor.screen.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.sun.imageeditor.R
import com.sun.imageeditor.data.model.Photo
import com.sun.imageeditor.databinding.ItemCollectionRecyclerViewBinding
import com.sun.imageeditor.databinding.ItemContainerPhotoBinding
import com.sun.imageeditor.databinding.ItemMainTitleBinding
import com.sun.imageeditor.utils.Constant
import com.sun.imageeditor.utils.OnItemRecyclerViewClickListener
import com.sun.imageeditor.utils.ext.loadImageWithUrl

class HomeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mData = mutableListOf<Any>()
    private var mOnPhotoClick: OnItemRecyclerViewClickListener<String>? = null

    private var mOnCollectionScrollListener: RecyclerView.OnScrollListener? = null
    private var mPhotoCollectionAdapter: CollectionCoverPhotoAdapter? = null
    var isPhotoLoading: Boolean = true
    var initialDataIndex: Int = 0

    fun setOnPhotoClick(onClick: OnItemRecyclerViewClickListener<String>) {
        mOnPhotoClick = onClick
    }

    fun setOnCollectionScrollListener(listener: RecyclerView.OnScrollListener) {
        mOnCollectionScrollListener = listener
    }

    fun setPhotoCollectionAdapter(adapter: CollectionCoverPhotoAdapter) {
        mPhotoCollectionAdapter = adapter
    }

    fun setInitialData(list: List<Any>) {
        mData.clear()
        mData.addAll(list)
        initialDataIndex = list.size
    }

    fun addPhotos(list: MutableList<Photo>) {
        val startPosition = mData.size
        mData.addAll(list)
        notifyItemRangeInserted(startPosition, list.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_VIEW_TITLE -> {
                TitleViewHolder(ItemMainTitleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ))
            }
            TYPE_VIEW_RECYCLER_VIEW -> {
                PhotoCollectionRecyclerViewHolder(ItemCollectionRecyclerViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ))
            }
            TYPE_VIEW_ITEM -> {
                PhotoViewHolder(ItemContainerPhotoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ))
            }
            else -> throw IllegalStateException(ILLEGAL_VIEW_TYPE_ERROR)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType) {
            TYPE_VIEW_ITEM -> {
                (mData[position] as? Photo)?.let {
                    (holder as? PhotoViewHolder)?.bind(
                        it,
                        position - initialDataIndex
                    )
                }
            }
            TYPE_VIEW_TITLE -> {
                (mData[position] as? String)?.let { (holder as? TitleViewHolder)?.bind(it) }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(mData[position]) {
            Constant.COLLECTION_COVER_PHOTO -> TYPE_VIEW_RECYCLER_VIEW
            is String -> TYPE_VIEW_TITLE
            else -> TYPE_VIEW_ITEM
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if (holder is PhotoViewHolder) holder.onLoading()
    }

    inner class PhotoCollectionRecyclerViewHolder(
        private val binding: ItemCollectionRecyclerViewBinding
        ): RecyclerView.ViewHolder(binding.root) {
        init {
            mPhotoCollectionAdapter?.let {
                binding.recyclerCollection.adapter = mPhotoCollectionAdapter
                mOnCollectionScrollListener?.let { listener ->
                    binding.recyclerCollection.addOnScrollListener(listener)
                }
            }
        }
    }

    inner class TitleViewHolder(private val binding: ItemMainTitleBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(title: String) {
            binding.textSectionTitle.text = title
        }
    }

    inner class PhotoViewHolder(
        private val binding: ItemContainerPhotoBinding
        ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Photo, itemOffset: Int) {
            val margin = calculateGridItemsMargin(itemOffset)
            val layoutParams = binding.cardPhoto.layoutParams as? ViewGroup.MarginLayoutParams
            layoutParams?.setMargins(margin.first, 0, margin.second, 0)

            binding.cardPhoto.layoutParams = layoutParams

            binding.imagePhoto.let {
                it.setOnClickListener {
                    mOnPhotoClick?.onItemClick(item.photoFullUrl)
                }
                it.isClickable = true
                it.loadImageWithUrl(item.photoThumbUrl, R.color.light_grey)
            }
        }

        fun onLoading() {
            binding.imagePhoto.isClickable = false
        }

        private fun calculateGridItemsMargin(itemOffset: Int) : Pair<Int, Int> {
            val context = binding.root.context
            val marginLeftRes = if (itemOffset % 2 == 0) R.dimen.dp_16 else R.dimen.dp_8
            val marginRightRes = if (itemOffset % 2 == 0) R.dimen.dp_8 else R.dimen.dp_16
            val marginLeft = context.resources.getDimensionPixelSize(marginLeftRes)
            val marginRight = context.resources.getDimensionPixelSize(marginRightRes)
            return Pair(marginLeft, marginRight)
        }
    }

    companion object {
        const val TYPE_VIEW_ITEM = 0
        const val TYPE_VIEW_TITLE = 2
        const val TYPE_VIEW_RECYCLER_VIEW = 3

        const val ILLEGAL_VIEW_TYPE_ERROR = "ILLEGAL_VIEW_TYPE_ERROR"
    }
}
