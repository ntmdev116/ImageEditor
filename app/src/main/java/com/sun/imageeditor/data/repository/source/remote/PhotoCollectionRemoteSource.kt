package com.sun.imageeditor.data.repository.source.remote

import com.sun.imageeditor.data.model.Photo
import com.sun.imageeditor.data.model.PhotoCollection
import com.sun.imageeditor.data.model.PhotoCollectionEntry
import com.sun.imageeditor.data.model.PhotoEntry
import com.sun.imageeditor.data.repository.source.OnResultListener
import com.sun.imageeditor.data.repository.source.PhotoCollectionDataSource
import com.sun.imageeditor.data.repository.source.remote.fetchjson.GetJsonFromUrl
import com.sun.imageeditor.utils.ApiHelper

class PhotoCollectionRemoteSource: PhotoCollectionDataSource.Remote {
    override fun getPhotoCollections(
        listener: OnResultListener<MutableList<PhotoCollection>>,
        page: Int,
        perPage: Int
    ) {
        GetJsonFromUrl(
            ApiHelper().getCollectionUrl(page, perPage),
            PhotoCollectionEntry.PHOTO_COLLECTION,
            listener
        )
    }

    override fun searchPhotoCollections(
        listener: OnResultListener<MutableList<PhotoCollection>>,
        page: Int,
        perPage: Int,
        query: String
    ) {
        GetJsonFromUrl(
            ApiHelper().getSearchCollectionUrl(query, page, perPage),
            PhotoCollectionEntry.SEARCH_PHOTO_COLLECTION,
            listener
        )
    }

    override fun getPhotos(
        listener: OnResultListener<MutableList<Photo>>,
        page: Int,
        perPage: Int
    ) {
        GetJsonFromUrl(
            ApiHelper().getPhotoUrl(page, perPage),
            PhotoEntry.PHOTO,
            listener
        )
    }

    override fun searchPhotos(
        listener: OnResultListener<MutableList<Photo>>,
        page: Int,
        perPage: Int,
        query: String
    ) {
        GetJsonFromUrl(
            ApiHelper().getSearchPhotoUrl(query, page, perPage),
            PhotoEntry.SEARCH_PHOTO,
            listener
        )
    }

    companion object {
        private var instance: PhotoCollectionRemoteSource? = null

        fun getInstance() = synchronized(this) {
            instance ?: PhotoCollectionRemoteSource().also { instance = it }
        }
    }

}
