package com.sun.imageeditor.data.repository.source

import com.sun.imageeditor.data.model.Photo
import com.sun.imageeditor.data.model.PhotoCollection

class PhotoCollectionDataSource {
    interface Remote {
        fun getPhotoCollections(
            listener: OnResultListener<MutableList<PhotoCollection>>,
            page: Int,
            perPage: Int
        )

        fun searchPhotoCollections(
            listener: OnResultListener<MutableList<PhotoCollection>>,
            page: Int,
            perPage: Int,
            query: String
        )

        fun getPhotos(
            listener: OnResultListener<MutableList<Photo>>,
            page: Int,
            perPage: Int
        )

        fun searchPhotos(
            listener: OnResultListener<MutableList<Photo>>,
            page: Int,
            perPage: Int,
            query: String
        )
    }
}
