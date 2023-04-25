package com.sun.imageeditor.screen.detail

import android.content.Context

class PhotoDetailContract {
    interface Presenter {
        fun downloadImage(context: Context?, url: String)
    }
}
