package com.sun.imageeditor.data.repository.source.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.sun.imageeditor.data.repository.source.OnResultListener
import com.sun.imageeditor.data.repository.source.RecentSearchDataSource
import java.io.IOException
import java.util.concurrent.Executors

class RecentSearchDatabase(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
), RecentSearchDataSource.Local {

    private val mExecutor = Executors.newSingleThreadExecutor()

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS " +
                RecentSearchTable.NAME +
                "(${RecentSearchTable.Cols.ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${RecentSearchTable.Cols.QUERY} TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // TODO Not yet implemented
    }

    override fun getRecentSearch(
        listener: OnResultListener<List<String>>,
        page: Int,
        perPage: Int,
    ) {
        mExecutor.execute { handleGetRecentSearch(listener, page, perPage) }
    }

    private fun handleGetRecentSearch(
        listener: OnResultListener<List<String>>,
        page: Int,
        perPage: Int,
    ) {
        val offset = (page-1) * perPage
        val query = "SELECT ${RecentSearchTable.Cols.QUERY} " +
                "FROM ${RecentSearchTable.NAME} " +
                "ORDER BY ${RecentSearchTable.Cols.ID} DESC " +
                "LIMIT $perPage " +
                "OFFSET $offset"

        try {
            val cursor = readableDatabase.rawQuery(query, null)
            val recentSearchList = mutableListOf<String>()

            cursor?.use {
                while (it.moveToNext()) {
                    val searchQuery = it.getString(it.getColumnIndexOrThrow(RecentSearchTable.Cols.QUERY))
                    recentSearchList.add(searchQuery)
                }
            }

            listener.onSuccess(recentSearchList)
        } catch (e: SQLiteException) {
            listener.onFail("${e.message}")
        } catch (e: IOException) {
            listener.onFail("${e.message}")
        }
    }

    override fun deleteQuery(
        listener: OnResultListener<Boolean>?,
        query: String
    ) {
        mExecutor.execute { handleDeleteQuery(listener, query) }
    }

    override fun addQuery(
        listener: OnResultListener<String>?,
        query: String
    ) {
        mExecutor.execute { handleAddQuery(listener, query) }
    }

    private fun handleDeleteQuery(
        listener: OnResultListener<Boolean>?,
        query: String
    ) {
        val whereClause = "${RecentSearchTable.Cols.QUERY}=?"
        val whereArgs = arrayOf(query)

        val rowsDeleted =
            writableDatabase.delete(RecentSearchTable.NAME, whereClause, whereArgs)

        if (rowsDeleted == 1) {
            listener?.onSuccess(true)
        } else {
            listener?.onFail(ROW_NOT_DELETE)
        }
    }

    private fun handleAddQuery(
        listener: OnResultListener<String>?,
        query: String
    ) {
        val value = ContentValues().apply {
            put(RecentSearchTable.Cols.QUERY, query)
        }
        try {
            writableDatabase.insertOrThrow(RecentSearchTable.NAME, null, value)
            listener?.onSuccess(query)
        } catch (e: SQLiteException) {
            listener?.onFail("${e.message}")
        } catch (e: IOException) {
            listener?.onFail("${e.message}")
        }
    }

    companion object {
        const val DATABASE_NAME = "search_history.db"
        const val DATABASE_VERSION = 1
        const val ROW_NOT_DELETE = "ROW_NOT_DELETE"

        private var instance: RecentSearchDatabase? = null

        fun getInstance(context: Context) = synchronized(this) {
            instance ?: RecentSearchDatabase(context).also { instance = it }
        }
    }

}
