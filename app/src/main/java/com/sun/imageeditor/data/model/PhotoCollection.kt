package com.sun.imageeditor.data.model

data class PhotoCollection(
    val id: String,
    val title: String,
    val coverPhotoUrl: String,
    val photoUrlList: MutableList<String>)
