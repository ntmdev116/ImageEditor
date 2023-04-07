package com.sun.imageeditor.screen.home

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sun.imageeditor.R
import com.sun.imageeditor.data.model.Photo
import com.sun.imageeditor.data.model.PhotoCollection
import com.sun.imageeditor.data.repository.PhotoCollectionRepository
import com.sun.imageeditor.data.repository.source.remote.PhotoCollectionRemoteSource
import com.sun.imageeditor.databinding.FragmentHomeBinding
import com.sun.imageeditor.screen.home.adapter.CollectionCoverPhotoAdapter
import com.sun.imageeditor.screen.home.adapter.HomeAdapter
import com.sun.imageeditor.utils.Constant
import com.sun.imageeditor.utils.base.BaseFragment


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
                            else -> throw IllegalStateException()
                        }
                    }
                }
            adapter = mHomeAdapter
        }

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

    override fun onError(msg: String?) {
        mHomeAdapter.isPhotoLoading = false
        mCollectionAdapter.isLoading = false
    }

    companion object {
        fun newInstance() =
            HomeFragment()
    }
}
