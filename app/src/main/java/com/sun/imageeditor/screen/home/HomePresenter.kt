package com.sun.imageeditor.screen.home

import android.os.Bundle
import com.sun.imageeditor.data.model.Photo
import com.sun.imageeditor.data.model.PhotoCollection
import com.sun.imageeditor.data.repository.PhotoCollectionRepository
import com.sun.imageeditor.data.repository.source.OnResultListener
import com.sun.imageeditor.screen.collectiondetail.CollectionDetailActivity
import com.sun.imageeditor.screen.search.SearchActivity
import com.sun.imageeditor.utils.Constant
import com.sun.imageeditor.utils.base.BasePresenter

class HomePresenter internal constructor(private val photoCollectionRepository: PhotoCollectionRepository?) :
    BasePresenter, HomeContract.Presenter {

    private var mView: HomeContract.View? = null
    private var mCurrentPhotoPage = 1
    private var mCurrentCollectionPage = 1

    override fun onStart() {
        // TODO Not yet implemented
    }

    override fun onStop() {
        // TODO Not yet implemented
    }

    fun onPhotoClick(url: String?) {
        mView?.showPhoto(url)
    }

    fun onCollectionClick(id: String?) {
        mView?.startNewActivity(
            CollectionDetailActivity::class.java,
            Bundle().apply { putString(Constant.COLLECTION_ID_KEY, id) }
        )
    }

    fun onSearchButtonClick() {
        mView?.startNewActivity(SearchActivity::class.java)
    }

    override fun getPhotoCollections() {
        photoCollectionRepository?.getPhotoCollections(
            object : OnResultListener<MutableList<PhotoCollection>> {
                override fun onSuccess(result: MutableList<PhotoCollection>) {
                    mView?.onGetPhotoCollectionsSuccess(result)
                    mCurrentCollectionPage += 1
                }

                override fun onFail(msg: String) {
                    mView?.onError(msg)
                }

            },
            page = mCurrentCollectionPage,
            perPage = DEFAULT_PER_PAGE
        )
    }

    override fun getPhotos() {
        photoCollectionRepository?.getPhotos(
            object : OnResultListener<MutableList<Photo>> {
                override fun onSuccess(result: MutableList<Photo>) {
                    mView?.onGetPhotosSuccess(result)
                    mCurrentPhotoPage += 1
                }

                override fun onFail(msg: String) {
                    mView?.onError(msg)
                }

            },
            page = mCurrentPhotoPage,
            perPage = DEFAULT_PER_PAGE
        )
    }

    override fun setView(view: HomeContract.View) {
        mView = view
    }

    companion object {
        private const val DEFAULT_PER_PAGE = 10
    }
}
