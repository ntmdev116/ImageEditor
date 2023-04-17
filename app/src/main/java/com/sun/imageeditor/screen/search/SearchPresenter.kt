package com.sun.imageeditor.screen.search

import android.os.Bundle
import com.sun.imageeditor.data.model.Photo
import com.sun.imageeditor.data.model.PhotoCollection
import com.sun.imageeditor.data.repository.PhotoCollectionRepository
import com.sun.imageeditor.data.repository.source.OnResultListener
import com.sun.imageeditor.screen.collectiondetail.CollectionDetailActivity
import com.sun.imageeditor.utils.Constant
import com.sun.imageeditor.utils.SearchType
import com.sun.imageeditor.utils.base.BasePresenter

class SearchPresenter internal constructor(private val photoCollectionRepository: PhotoCollectionRepository?) :
    BasePresenter, SearchContract.Presenter {
    private var mView: SearchContract.ActivityView? = null
    private var mCurrentQuery = ""
    private val mCurrentPageOnTab = mutableListOf<Int>()

    fun onCollectionClick(id: String) {
        mView?.startNewActivity(
            CollectionDetailActivity::class.java,
            Bundle().apply { putString(Constant.COLLECTION_ID_KEY, id) }
        )
    }

    fun onPhotoClick(url: String) {
        mView?.showPhoto(url)
    }

    fun onRecentSearchClick() {
        // TODO Not yet implemented
    }

    override fun searchPhotoCollections(index: Int, query: String) {
        photoCollectionRepository?.searchPhotoCollections(
            object : OnResultListener<MutableList<PhotoCollection>> {
                override fun onSuccess(list: MutableList<PhotoCollection>) {
                    mView?.onGetItemsSuccess(
                        list,
                        tabIndex =  index,
                        isFirstPage = mCurrentPageOnTab[index] == 1
                    )
                    mCurrentPageOnTab[index] += 1
                }

                override fun onFail(msg: String) {
                    mView?.onError(msg, index)
                }

            },
            page = mCurrentPageOnTab[index],
            perPage = DEFAULT_PER_PAGE,
            query
        )
    }
    override fun searchPhotos(index: Int, query: String) {
        photoCollectionRepository?.searchPhotos(
            object : OnResultListener<MutableList<Photo>> {
                override fun onSuccess(list: MutableList<Photo>) {
                    mView?.onGetItemsSuccess(list, index, mCurrentPageOnTab[index] == 1)
                    mCurrentPageOnTab[index] += 1
                }

                override fun onFail(msg: String) {
                    mView?.onError(msg, index)
                }

            },
            page = mCurrentPageOnTab[index],
            perPage = DEFAULT_PER_PAGE,
            query
        )
    }

    override fun getRecentSearch() {
        // TODO Not yet implemented
    }

    override fun setView(view: SearchContract.ActivityView, tabCount: Int) {
        mView = view
        mCurrentPageOnTab.clear()
        (1..tabCount).forEach { _ ->
            mCurrentPageOnTab.add(1)
        }
    }

    override fun search(searchType: SearchType?, tabIndex: Int, query: String) {
        if (query != mCurrentQuery) {
            mCurrentPageOnTab.fill(1)
            mCurrentQuery = query
        }
        if (tabIndex < mCurrentPageOnTab.size)
            when (searchType) {
                SearchType.PHOTO -> { searchPhotos(tabIndex, query) }
                SearchType.COLLECTION -> { searchPhotoCollections(tabIndex, query) }
                else -> {}
            }
    }

    override fun onStart() {
        // TODO Not yet implemented
    }

    override fun onStop() {
        // TODO Not yet implemented
    }

    companion object {
        private const val DEFAULT_PER_PAGE = 10
    }
}
