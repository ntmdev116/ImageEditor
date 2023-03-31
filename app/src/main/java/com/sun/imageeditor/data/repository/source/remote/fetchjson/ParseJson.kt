package com.sun.imageeditor.data.repository.source.remote.fetchjson

import com.sun.imageeditor.data.model.Photo
import com.sun.imageeditor.data.model.PhotoCollection
import com.sun.imageeditor.data.model.PhotoCollectionEntry
import com.sun.imageeditor.data.model.PhotoEntry
import org.json.JSONObject

class ParseJson {
    fun photoCollectionParseJson(jsonObject: JSONObject): PhotoCollection {
        val coverPhotoUrl = jsonObject
            .getJSONObject(PhotoCollectionEntry.COVER_PHOTO)
            .getJSONObject(PhotoCollectionEntry.URLS)
            .getString(PhotoCollectionEntry.THUMB)

        return PhotoCollection(
            id = jsonObject.optString(PhotoCollectionEntry.ID),
            title = jsonObject.optString(PhotoCollectionEntry.TITLE),
            coverPhotoUrl = coverPhotoUrl ?: ""
        )
    }

    fun photoParseJson(jsonObject: JSONObject): Photo {
        return Photo(
            id = jsonObject.getString(PhotoEntry.ID),
            description = jsonObject.optString(PhotoEntry.DESCRIPTION),
            altDescription = jsonObject.optString(PhotoEntry.ALT_DESCRIPTION),
            photoUrl = jsonObject.getJSONObject(PhotoEntry.LINKS)
                .getString(PhotoEntry.DOWNLOAD_LOCATION)
        )
    }
}
