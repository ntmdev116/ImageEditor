package com.sun.imageeditor.utils

import android.content.Context
import android.graphics.Bitmap
import com.sun.imageeditor.data.repository.source.OnResultListener
import java.util.concurrent.Executors
import com.mukesh.image_processing.ImageProcessor
import com.sun.imageeditor.utils.ext.reducedBitmap
import com.sun.imageeditor.utils.filters.GrayscaleEffectFilter
import com.sun.imageeditor.utils.filters.PixelateEffectFilter
import com.sun.imageeditor.utils.filters.SepiaEffectFilter
import com.sun.imageeditor.utils.filters.VignetteEffectFilter

class ImageProcessor(private val originalImage: Bitmap) {

    var onResultListener: OnResultListener<Bitmap>? = null

    private val mExecutor by lazy { Executors.newCachedThreadPool() }
    private val mProcessor: ImageProcessor by lazy {
        ImageProcessor()
    }

    private val mPipeLines = mutableListOf<Pair<EditType, Bitmap>>(
        Pair(EditType.ORIGINAL, originalImage),
        Pair(EditType.FILTER, originalImage),
        Pair(EditType.ADJUST, originalImage),
        Pair(EditType.ICON, originalImage),
        Pair(EditType.DRAW, originalImage),
        Pair(EditType.CROP, originalImage),
    )

    fun edit(editType: EditType, editParameters: EditParameters) {
        val editTypeIndex = mPipeLines.indexOfFirst { it.first == editType }
        mExecutor.execute {
            when (editType) {
                EditType.FILTER -> {
                    val bm = filter(mPipeLines[editTypeIndex - 1].second, editParameters.filterType)
                    mPipeLines[editTypeIndex] = mPipeLines[editTypeIndex].copy(second = bm)

                    onResultListener?.onSuccess(bm)
                }
                else -> {}
            }
        }
    }

    fun getFilterPreview(filterType: FilterType, listener: OnResultListener<Bitmap>) {
        mExecutor.execute {
            val originalIndex = mPipeLines.indexOfFirst { it.first == EditType.ORIGINAL }

            val bm = filter(mPipeLines[originalIndex].second
                .reducedBitmap(Constant.THUMBNAIL_REDUCE_SIZE), filterType)

            listener.onSuccess(bm)
        }
    }

    private fun filter(bitmap: Bitmap, filterType: FilterType?): Bitmap {
        return when (filterType) {
            FilterType.PIXELATE -> {
                PixelateEffectFilter().apply(bitmap)
            }
            FilterType.SEPIA -> {
                SepiaEffectFilter().apply(bitmap)
            }
            FilterType.GRAYSCALE -> {
                GrayscaleEffectFilter().apply(bitmap)
            }
            FilterType.SNOW -> {
                mProcessor.applySnowEffect(bitmap)
            }
            FilterType.VIGNETTE -> {
                VignetteEffectFilter().apply(bitmap)
            }
            else -> { bitmap }
        }
    }


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

    companion object {
        const val ERROR_MESSAGE = "Error in image processing"
    }
}
