package com.sun.imageeditor.screen.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sun.imageeditor.R
import com.sun.imageeditor.data.model.Photo
import com.sun.imageeditor.data.model.PhotoCollection
import com.sun.imageeditor.data.repository.PhotoCollectionRepository
import com.sun.imageeditor.data.repository.source.remote.PhotoCollectionRemoteSource
import com.sun.imageeditor.databinding.FragmentHomeBinding
import com.sun.imageeditor.screen.detail.PhotoDetailFragment
import com.sun.imageeditor.screen.home.adapter.CollectionCoverPhotoAdapter
import com.sun.imageeditor.screen.home.adapter.HomeAdapter
import com.sun.imageeditor.utils.Constant
import com.sun.imageeditor.utils.OnItemRecyclerViewClickListener
import com.sun.imageeditor.utils.base.BaseFragment
import com.sun.imageeditor.utils.ext.addFragment


class HomeFragment :
    BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate), HomeContract.View
{
    private val mCollectionAdapter by lazy { CollectionCoverPhotoAdapter() }
    private val mHomeAdapter by lazy { HomeAdapter() }
    private val mData by lazy {
        listOf(
            getString(R.string.trending_collection),
            Constant.COLLECTION_COVER_PHOTO,
            getString(R.string.trending_photos),
        )
    }
    private val mPresenter by lazy {
        HomePresenter(
            PhotoCollectionRepository.getInstance(
                PhotoCollectionRemoteSource.getInstance()
            ))
    }

    override fun initData() {
        mPresenter.setView(this)
        mPresenter.getPhotoCollections()
        mPresenter.getPhotos()
    }

    override fun initView() {
        binding.buttonSearch.setOnClickListener { mPresenter.onSearchButtonClick() }

        mCollectionAdapter.setOnItemClick(object : OnItemRecyclerViewClickListener<String> {
            override fun onItemClick(parameter: String?) {
                mPresenter.onCollectionClick(parameter)
            }
        })

        mHomeAdapter.setOnCollectionScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollHorizontally(1) && !mCollectionAdapter.isLoading) {
                    mCollectionAdapter.isLoading = true
                    mPresenter.getPhotoCollections()
                }
            }
        })

        binding.recyclerHome.run {
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (!recyclerView.canScrollVertically(1) && !mHomeAdapter.isPhotoLoading) {
                        mHomeAdapter.isPhotoLoading = true
                        mPresenter.getPhotos()
                    }
                }
            })

            (layoutManager as? GridLayoutManager)?.spanSizeLookup =
                object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when (mHomeAdapter.getItemViewType(position)) {
                            HomeAdapter.TYPE_VIEW_RECYCLER_VIEW, HomeAdapter.TYPE_VIEW_TITLE -> 2
                            HomeAdapter.TYPE_VIEW_ITEM -> 1
                            else -> throw IllegalStateException(HomeAdapter.ILLEGAL_VIEW_TYPE_ERROR)
                        }
                    }
                }
            adapter = mHomeAdapter
        }

        mHomeAdapter.setOnPhotoClick(object: OnItemRecyclerViewClickListener<String> {
            override fun onItemClick(parameter: String?) {
                mPresenter.onPhotoClick(parameter)
            }
        })
        mHomeAdapter.setPhotoCollectionAdapter(mCollectionAdapter)
        mHomeAdapter.setInitialData(mData)
    }

    override fun onGetPhotoCollectionsSuccess(collections: MutableList<PhotoCollection>) {
        mCollectionAdapter.addCoverPhotos(collections)
        mCollectionAdapter.isLoading = false
    }

    override fun onGetPhotosSuccess(photos: MutableList<Photo>) {
        mHomeAdapter.addPhotos(photos)
        mHomeAdapter.isPhotoLoading = false
    }

    override fun showPhoto(url: String?) {
        addFragment(binding.root.id, PhotoDetailFragment.newInstance())
    }

    override fun startNewActivity(activityClass: Class<*>, bundle: Bundle?) {
        startActivity(
            Intent(context, activityClass).apply { bundle?.let { putExtras(it) } }
        )
    }

    override fun onError(msg: String?) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            mHomeAdapter.isPhotoLoading = false
            mCollectionAdapter.isLoading = false
        }
    }

    companion object {
        fun newInstance() =
            HomeFragment()
    }
}
