package com.sun.imageeditor.data.repository.source.remote.fetchjson

import android.os.Handler
import android.os.Looper
import com.sun.imageeditor.data.repository.source.OnResultListener
import com.sun.imageeditor.utils.ApiConstant
import org.brotli.dec.BrotliInputStream
import org.json.JSONException
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
    private var exception: Exception? = null
    private var data: T? = null

    init {
        callAPI()
    }

    private fun callAPI() {
        mExecutor.execute {
            try {
                val responseJson =
                    getJsonFromUrl(urlString)
                data = ParseDataWithJson().parseJsonToData(responseJson, keyEntity) as? T

                mHandler.post {
                    try {
                        data?.let { listener.onSuccess(it) }
                    } catch (e: Exception) {
                        listener.onFail(e.toString())
                    }
                }
            } catch (e: JSONException) {
                listener.onFail(e.toString())
            } catch (e: IOException) {
                listener.onFail(ApiConstant.CONNECTION_ERROR)
            }
        }
    }

    private fun getJsonFromUrl(urlString: String): String {
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
