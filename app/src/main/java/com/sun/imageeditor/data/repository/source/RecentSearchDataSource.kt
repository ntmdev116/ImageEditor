package com.sun.imageeditor.data.repository.source

class RecentSearchDataSource {
    interface Local {
        fun getRecentSearch(
            listener: OnResultListener<List<String>>,
            page: Int,
            perPage: Int,
        )

        fun deleteQuery(
            listener: OnResultListener<Boolean>?,
            query: String
        )

        fun addQuery(
            listener: OnResultListener<String>?,
            query: String,
        )
    }
}
