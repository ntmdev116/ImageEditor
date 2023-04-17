package com.sun.imageeditor.screen.search

import android.os.Bundle
import com.sun.imageeditor.data.model.Photo
import com.sun.imageeditor.data.model.PhotoCollection
import com.sun.imageeditor.utils.SearchType

class SearchContract {
    interface View {
        fun clearResult()
        fun onError(msg: String?)
    }
    interface ActivityView {
        fun search()
        fun showPhoto(url: String?)
        fun startNewActivity(activityClass: Class<*>, bundle: Bundle? = null)
        fun <T: Any> onGetItemsSuccess(items: MutableList<T>, tabIndex: Int, isFirstPage: Boolean)
        fun onGetRecentSearchSuccess(queries: MutableList<String>)
        fun onError(msg: String?, tabIndex: Int)
    }

    interface Presenter {
        fun searchPhotoCollections(index: Int, query: String)
        fun searchPhotos(index: Int, query: String)
        fun search(searchType: SearchType?, tabIndex: Int, query: String)
        fun getRecentSearch()
        fun setView(view: ActivityView, tabCount: Int)
    }
}
