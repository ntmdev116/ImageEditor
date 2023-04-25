package com.sun.imageeditor.data.repository

import com.sun.imageeditor.data.repository.source.OnResultListener
import com.sun.imageeditor.data.repository.source.RecentSearchDataSource

class RecentSearchRepository(
    private val local: RecentSearchDataSource.Local
) : RecentSearchDataSource.Local {

    override fun getRecentSearch(
        listener: OnResultListener<List<String>>,
        page: Int,
        perPage: Int,
    ) {
        local.getRecentSearch(listener, page, perPage)
    }

    override fun deleteQuery(listener: OnResultListener<Boolean>?, query: String) {
        local.deleteQuery(listener, query)
    }

    override fun addQuery(listener: OnResultListener<String>?, query: String) {
        local.addQuery(listener, query)
    }

    fun deleteThenAddQuery(listener: OnResultListener<String>?, query: String) {
        deleteQuery(
            object : OnResultListener<Boolean> {
                override fun onSuccess(result: Boolean) {
                    local.addQuery(listener, query)
                }

                override fun onFail(msg: String) {
                    local.addQuery(listener, query)
                }
            },
            query
        )
    }

    companion object {
        private var instance: RecentSearchRepository? = null

        fun getInstance(local: RecentSearchDataSource.Local) =
            synchronized(this) {
                instance ?: RecentSearchRepository(local).also { instance = it }
            }
    }
}

