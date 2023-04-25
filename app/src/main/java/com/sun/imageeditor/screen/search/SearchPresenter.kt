package com.sun.imageeditor.screen.search

import android.os.Bundle
import com.sun.imageeditor.data.model.Photo
import com.sun.imageeditor.data.model.PhotoCollection
import com.sun.imageeditor.data.repository.PhotoCollectionRepository
import com.sun.imageeditor.data.repository.RecentSearchRepository
import com.sun.imageeditor.data.repository.source.OnResultListener
import com.sun.imageeditor.screen.collectiondetail.CollectionDetailActivity
import com.sun.imageeditor.utils.Constant
import com.sun.imageeditor.utils.SearchType
import com.sun.imageeditor.utils.base.BasePresenter

class SearchPresenter internal constructor(
    private val photoCollectionRepository: PhotoCollectionRepository?,
    private val recentSearchRepository: RecentSearchRepository?
) : BasePresenter, SearchContract.Presenter {

    private var mView: SearchContract.ActivityView? = null
    private var mCurrentQuery = ""
    private var mCurrentRecentSearchPage = DEFAULT_FIRST_PAGE
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

    override fun searchPhotoCollections(index: Int, query: String) {
        photoCollectionRepository?.searchPhotoCollections(
            object : OnResultListener<MutableList<PhotoCollection>> {
                override fun onSuccess(result: MutableList<PhotoCollection>) {
                    mView?.onGetItemsSuccess(
                        result,
                        index,
                        mCurrentPageOnTab[index] == DEFAULT_FIRST_PAGE
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
                override fun onSuccess(result: MutableList<Photo>) {
                    mView?.onGetItemsSuccess(
                        result,
                        index,
                        mCurrentPageOnTab[index] == DEFAULT_FIRST_PAGE
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

    override fun getRecentSearch() {
        recentSearchRepository?.getRecentSearch(
            object : OnResultListener<List<String>> {
                override fun onSuccess(result: List<String>) {
                    mView?.onGetRecentSearchSuccess(
                        result,
                        mCurrentRecentSearchPage == DEFAULT_FIRST_PAGE
                    )

                    mCurrentRecentSearchPage++
                }

                override fun onFail(msg: String) {
                    // TODO Not yet implemented
                }

            },
            page = mCurrentRecentSearchPage,
            perPage = DEFAULT_PER_PAGE,
        )
    }

    override fun setView(view: SearchContract.ActivityView, tabCount: Int) {
        mView = view
        mCurrentPageOnTab.clear()
        (1..tabCount).forEach { _ ->
            mCurrentPageOnTab.add(DEFAULT_FIRST_PAGE)
        }
    }

    override fun search(searchType: SearchType?, tabIndex: Int, query: String) {
        if (query != mCurrentQuery) {
            mCurrentPageOnTab.fill(DEFAULT_FIRST_PAGE)
            mCurrentRecentSearchPage = DEFAULT_FIRST_PAGE
            mCurrentQuery = query

            recentSearchRepository?.deleteThenAddQuery(
                object : OnResultListener<String> {
                    override fun onSuccess(result: String) {
                        getRecentSearch()
                    }

                    override fun onFail(msg: String) {
                        // TODO Not yet implemented
                    }

                },
                query
            )
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
        private const val DEFAULT_FIRST_PAGE = 1
    }
}
