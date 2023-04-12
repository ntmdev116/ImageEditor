package com.sun.imageeditor.screen.search

import com.sun.imageeditor.data.model.Photo
import com.sun.imageeditor.data.model.PhotoCollection

class SearchContract {
    interface View {
        fun onGetPhotoCollectionsSuccess(collections: MutableList<PhotoCollection>)
        fun onGetPhotosSuccess(photos: MutableList<Photo>)
        fun onError(msg: String?)
    }

    interface Presenter {
        fun getPhotoCollections()
        fun getPhotos()
        fun setView(view: View)
    }
}
