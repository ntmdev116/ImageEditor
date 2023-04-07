package com.sun.imageeditor.screen.home

import com.sun.imageeditor.data.model.Photo
import com.sun.imageeditor.data.model.PhotoCollection

class HomeContract {
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
