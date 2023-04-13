package com.sun.imageeditor.data.repository

import com.sun.imageeditor.data.model.Photo
import com.sun.imageeditor.data.model.PhotoCollection
import com.sun.imageeditor.data.repository.source.OnResultListener
import com.sun.imageeditor.data.repository.source.PhotoCollectionDataSource

class PhotoCollectionRepository(private val remote: PhotoCollectionDataSource.Remote): PhotoCollectionDataSource.Remote{
    override fun getPhotoCollections(
        listener: OnResultListener<MutableList<PhotoCollection>>,
        page: Int,
        perPage: Int
    ) {
        remote.getPhotoCollections(listener, page, perPage)
    }

    override fun searchPhotoCollections(
        listener: OnResultListener<MutableList<PhotoCollection>>,
        page: Int,
        perPage: Int,
        query: String
    ) {
        remote.searchPhotoCollections(listener, page, perPage, query)
    }

    override fun getCollectionPhotos(
        listener: OnResultListener<MutableList<Photo>>,
        id: String?,
        page: Int,
        perPage: Int
    ) {
        remote.getCollectionPhotos(listener, id, page, perPage)
    }

    override fun getPhotos(
        listener: OnResultListener<MutableList<Photo>>,
        page: Int,
        perPage: Int
    ) {
        remote.getPhotos(listener, page, perPage)
    }

    override fun searchPhotos(
        listener: OnResultListener<MutableList<Photo>>,
        page: Int,
        perPage: Int,
        query: String
    ) {
        remote.searchPhotos(listener, page, perPage, query)
    }

    companion object {
        private var instance: PhotoCollectionRepository? = null

        fun getInstance(remote: PhotoCollectionDataSource.Remote) =
            synchronized(this) {
                instance ?: PhotoCollectionRepository(remote).also { instance = it }
            }
    }
}
