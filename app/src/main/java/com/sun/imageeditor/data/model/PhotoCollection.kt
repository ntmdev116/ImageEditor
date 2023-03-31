package com.sun.imageeditor.data.model

data class PhotoCollection(
    val id: String,
    val title: String,
    val coverPhotoUrl: String,
)

object PhotoCollectionEntry {
    const val PHOTO_COLLECTION = "PHOTO_COLLECTION"
    const val SEARCH_PHOTO_COLLECTION = "SEARCH_PHOTO_COLLECTION"
    const val ID = "id"
    const val COVER_PHOTO = "cover_photo"
    const val TITLE = "title"
    const val URLS = "urls"
    const val THUMB = "thumb"
}
