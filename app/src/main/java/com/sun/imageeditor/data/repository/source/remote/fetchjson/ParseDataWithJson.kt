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
        val jsonTokener = JSONTokener(jsonString)

        val jsonElement = jsonTokener.nextValue()

        return when(jsonElement) {
            is JSONArray -> {
                val data = mutableListOf<Any>()
                for (i in 0 until (jsonElement.length() ?: 0)) {
                    val item = parseJsonToObject(jsonElement.getJSONObject(i), keyEntity)
                    if (item != null)
                        data.add(item)
                }

                if (data.size > 0) return data
                else null
            }
            is JSONObject -> {
                parseJsonToObject(jsonElement, keyEntity)
            }
            else -> null
        }
    }

    private fun parseJsonToObject(jsonElement: JSONObject?, keyEntity: String): Any? {
        return jsonElement?.let {
            when(keyEntity) {
                PhotoCollectionEntry.PHOTO_COLLECTION -> ParseJson().photoCollectionParseJson(it)
                PhotoEntry.PHOTO -> ParseJson().photoParseJson(it)
                else -> null
            }
        }
    }
}
