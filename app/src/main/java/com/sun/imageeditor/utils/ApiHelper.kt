package com.sun.imageeditor.utils


class ApiHelper {
    fun getCollectionUrl(page: Int = 1, perPage: Int = 10): String {
        return "${ApiConstant.COLLECTION_URL}?page=$page&per_page=$perPage"
    }

    fun getCollectionUrl(id: String): String {
        return "${ApiConstant.COLLECTION_URL}/$id"
    }

    fun getCollectionPhotosUrl(id: String?, page: Int = 1, perPage: Int = 10): String {
        return "${ApiConstant.COLLECTION_URL}/$id/photos?page=$page&per_page=$perPage"
    }

    fun getSearchCollectionUrl(query: String, page: Int = 1, perPage: Int = 10): String {
        return "${ApiConstant.SEARCH_COLLECTION_URL}?page=$page&per_page=$perPage&query=$query"
    }

    fun getPhotoUrl(page: Int = 1, perPage: Int = 10): String {
        return "${ApiConstant.PHOTO_URL}?page=$page&per_page=$perPage"
    }

    fun getPhotoUrl(id: String): String {
        return "${ApiConstant.PHOTO_URL}/$id"
    }

    fun getSearchPhotoUrl(query: String, page: Int = 1, perPage: Int = 10): String {
        return "${ApiConstant.SEARCH_PHOTO_URL}?page=$page&per_page=$perPage&query=$query"
    }
}
