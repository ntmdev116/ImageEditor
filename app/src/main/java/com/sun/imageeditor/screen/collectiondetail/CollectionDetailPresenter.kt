package com.sun.imageeditor.screen.collectiondetail

import com.sun.imageeditor.data.model.Photo
import com.sun.imageeditor.data.repository.PhotoCollectionRepository
import com.sun.imageeditor.data.repository.source.OnResultListener
import com.sun.imageeditor.utils.base.BasePresenter

class CollectionDetailPresenter internal constructor(
    private val photoCollectionRepository: PhotoCollectionRepository?
) : BasePresenter, CollectionDetailContract.Presenter {
    private var mView: CollectionDetailContract.View? = null
    private var mCurrentPage: Int = 1
    override fun getPhotoCollections() {
        // TODO Not yet implemented
    }

    override fun getPhotos() {
        photoCollectionRepository?.getCollectionPhotos(
            object : OnResultListener<MutableList<Photo>> {
                override fun onSuccess(result: MutableList<Photo>) {
                    mView?.onGetPhotosSuccess(result)
                    mCurrentPage++
                }

                override fun onFail(msg: String) {
                    mView?.onError(msg)
                }

            },
            mView?.getCollectionId(),
            mCurrentPage,
            DEFAULT_PER_PAGE
        )

    }

    override fun setView(view: CollectionDetailContract.View) {
        mView = view
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
