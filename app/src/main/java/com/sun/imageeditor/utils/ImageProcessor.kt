package com.sun.imageeditor.utils

import android.content.Context
import android.graphics.Bitmap
import com.sun.imageeditor.data.repository.source.OnResultListener
import java.util.concurrent.Executors

class ImageProcessor(private val originalImage: Bitmap) {
    private val mExecutor by lazy { Executors.newCachedThreadPool() }

    fun saveBitmap(
        fileName: String?,
        context: Context?,
        onResultListener: OnResultListener<String>,
    ) {
        mExecutor.execute {
            ImageSaver().saveBitmap(
                getProcessedImage(),
                fileName,
                context,
                onResultListener
            )
        }
    }

    // current for testing
    fun getProcessedImage(): Bitmap {
        return originalImage
    }

}
