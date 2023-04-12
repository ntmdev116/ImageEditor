package com.sun.imageeditor.screen.collectiondetail

import com.sun.imageeditor.data.repository.PhotoCollectionRepository
import com.sun.imageeditor.utils.base.BasePresenter

class CollectionDetailPresenter internal constructor(private val photoCollectionRepository: PhotoCollectionRepository?) :
    BasePresenter, CollectionDetailContract.Presenter {
    override fun getPhotoCollections() {
    }

    override fun getPhotos() {
    }

    override fun setView(view: CollectionDetailContract.View) {
    }

    override fun onStart() {
    }

    override fun onStop() {
    }
}
