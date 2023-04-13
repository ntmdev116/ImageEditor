package com.sun.imageeditor.screen.collectiondetail

import com.sun.imageeditor.data.model.Photo
import com.sun.imageeditor.data.model.PhotoCollection

class CollectionDetailContract {
    interface View {
        fun onGetPhotoCollectionsSuccess(collections: MutableList<PhotoCollection>)
        fun onGetPhotosSuccess(photos: MutableList<Photo>)
        fun onError(msg: String?)
        fun getCollectionId(): String?
    }

    interface Presenter {
        fun getPhotoCollections()
        fun getPhotos()
        fun setView(view: View)
    }
}
