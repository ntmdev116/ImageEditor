package com.sun.imageeditor.utils

import com.sun.imageeditor.BuildConfig

object ApiConstant {
    const val BASE_API_KEY = "Client-ID ${BuildConfig.API_KEY}"
    const val COLLECTION_URL = "https://api.unsplash.com/collections"
    const val SEARCH_COLLECTION_URL = "https://api.unsplash.com/search/collections"
    const val PHOTO_URL = "https://api.unsplash.com/photos"
    const val SEARCH_PHOTO_URL = "https://api.unsplash.com/search/photos"

    const val RESULT_ENTRY = "results"

    const val AUTHORIZATION_HEADER = "Authorization"

    const val CONNECTION_ERROR = "Connection Error"
    const val EMPTY_RESOURCE = "Empty Resource"
}
