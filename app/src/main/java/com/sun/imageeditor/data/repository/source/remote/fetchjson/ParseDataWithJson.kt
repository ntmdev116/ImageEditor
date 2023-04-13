package com.sun.imageeditor.data.repository.source.remote.fetchjson

import com.sun.imageeditor.data.model.PhotoCollectionEntry
import com.sun.imageeditor.data.model.PhotoEntry
import com.sun.imageeditor.utils.ApiConstant
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener

class ParseDataWithJson {
    fun parseJsonToData(jsonString: String?, keyEntity: String): Any? {
        when (keyEntity) {
            PhotoCollectionEntry.SEARCH_PHOTO_COLLECTION -> {
                val jsonObject = jsonString?.let { JSONObject(it) }
                return parseJsonToData(
                    jsonObject?.getJSONArray(ApiConstant.RESULT_ENTRY).toString(),
                    PhotoCollectionEntry.PHOTO_COLLECTION
                )
            }
            PhotoEntry.SEARCH_PHOTO -> {
                val jsonObject = jsonString?.let { JSONObject(it) }
                return parseJsonToData(
                    jsonObject?.getJSONObject(ApiConstant.RESULT_ENTRY).toString(),
                    PhotoEntry.PHOTO
                )
            }
        }

        val jsonTokener = JSONTokener(jsonString)

        val jsonElement = jsonTokener.nextValue()

        when(jsonElement) {
            is JSONArray -> {
                val data = mutableListOf<Any>()
                try {
                    for (i in 0 until (jsonElement.length() ?: 0)) {
                        val item = parseJsonToObject(jsonElement.getJSONObject(i), keyEntity)
                        item?.let {
                            data.add(it)
                        }
                    }

                    if (data.size > 0) return data
                    return null
                } catch (e: JSONException) {
                    throw e
                }
            }
            is JSONObject -> {
                try {
                    return parseJsonToObject(jsonElement, keyEntity)
                } catch (e: JSONException) {
                    throw e
                }
            }
        }

        return null
    }

    private fun parseJsonToObject(jsonElement: JSONObject?, keyEntity: String): Any? {
        try {
            jsonElement?.let {
                return when(keyEntity) {
                    PhotoCollectionEntry.PHOTO_COLLECTION -> ParseJson().photoCollectionParseJson(it)
                    PhotoEntry.PHOTO -> ParseJson().photoParseJson(it)
                    else -> null
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return null
    }
}
