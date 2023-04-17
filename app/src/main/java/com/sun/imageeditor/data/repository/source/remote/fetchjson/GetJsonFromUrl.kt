package com.sun.imageeditor.data.repository.source.remote.fetchjson

import android.os.Handler
import android.os.Looper
import com.sun.imageeditor.data.model.PhotoCollectionEntry
import com.sun.imageeditor.data.model.PhotoEntry
import com.sun.imageeditor.data.repository.source.OnResultListener
import com.sun.imageeditor.utils.ApiConstant
import org.brotli.dec.BrotliInputStream
import org.json.JSONException
import org.json.JSONObject
import java.io.FileNotFoundException
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class GetJsonFromUrl<T>(
    private val urlString: String,
    private val keyEntity: String,
    private val listener: OnResultListener<T>
) {

    private val mExecutor: Executor = Executors.newSingleThreadExecutor()
    private val mHandler = Handler(Looper.getMainLooper())
    private var data: T? = null

    init {
        callAPI()
    }

    private fun callAPI() {
        mExecutor.execute {
            try {
                val responseJson =
                    getJsonFromApi(urlString)

                val (jsonString, newKeyEntity) = convertKeyEntity(responseJson, keyEntity)
                data = ParseDataWithJson().parseJsonToData(jsonString, newKeyEntity) as? T

                mHandler.post {
                    if (data == null) listener.onFail(ApiConstant.EMPTY_RESOURCE)
                    else data?.let { listener.onSuccess(it) }
                }
            } catch (e: JSONException) {
                listener.onFail(e.toString())
            } catch (e: IOException) {
                listener.onFail("${ApiConstant.CONNECTION_ERROR}: ${e.message}")
            }
        }
    }

    private fun convertKeyEntity(jsonString: String?, keyEntity: String)
    : Pair<String?, String> {
        return when (keyEntity) {
            PhotoCollectionEntry.SEARCH_PHOTO_COLLECTION -> {
                val jsonObject = jsonString?.let { JSONObject(it) }
                Pair(
                    jsonObject?.getJSONArray(ApiConstant.RESULT_ENTRY).toString(),
                    PhotoCollectionEntry.PHOTO_COLLECTION
                )
            }
            PhotoEntry.SEARCH_PHOTO -> {
                val jsonObject = jsonString?.let { JSONObject(it) }
                Pair(
                    jsonObject?.getJSONArray(ApiConstant.RESULT_ENTRY).toString(),
                    PhotoEntry.PHOTO
                )
            }
            else -> Pair(jsonString, keyEntity)
        }
    }

    private fun getJsonFromApi(urlString: String): String {
        val url = URL(urlString)
        val httpURLConnection = url.openConnection() as? HttpURLConnection
        httpURLConnection?.run {
            connectTimeout = TIME_OUT
            readTimeout = TIME_OUT
            requestMethod = METHOD_GET
            doInput = true
            setRequestProperty(ApiConstant.AUTHORIZATION_HEADER, ApiConstant.BASE_API_KEY)
            setRequestProperty(ACCEPT, ACCEPT_ALL)
            setRequestProperty(ACCEPT_ENCODING, listOf(GZIP, DEFLATE, BR).joinToString())
            connect()
        }

        if (httpURLConnection?.responseCode == REQUEST_OK) {
            val inputStream = httpURLConnection.inputStream
            val responseBody = if (httpURLConnection.contentEncoding.equals(BR, ignoreCase = true)) {
                BrotliInputStream(inputStream)
            } else {
                inputStream
            }
            val response = responseBody?.bufferedReader().use { it?.readText() }

            httpURLConnection.disconnect()
            return response.toString()
        }

        httpURLConnection?.disconnect()
        throw FileNotFoundException()
    }

    companion object {
        private const val TIME_OUT = 15000
        private const val REQUEST_OK = 200
        private const val METHOD_GET = "GET"

        private const val ACCEPT_ENCODING = "Accept-Encoding"
        private const val ACCEPT = "Accept"
        private const val ACCEPT_ALL = "*/*"
        private const val GZIP = "gzip"
        private const val BR = "br"
        private const val DEFLATE = "deflate"
    }
}
