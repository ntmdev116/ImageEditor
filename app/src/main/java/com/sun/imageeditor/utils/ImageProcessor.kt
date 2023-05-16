package com.sun.imageeditor.utils

import android.content.Context
import android.graphics.Bitmap
import com.sun.imageeditor.data.repository.source.OnResultListener
import java.util.concurrent.Executors
import com.mukesh.image_processing.ImageProcessor
import com.sun.imageeditor.utils.adjust.BrightnessAdjust
import com.sun.imageeditor.utils.adjust.ContrastAdjust
import com.sun.imageeditor.utils.draw.IconDraw
import com.sun.imageeditor.utils.ext.reducedBitmap
import com.sun.imageeditor.utils.filters.GrayscaleEffectFilter
import com.sun.imageeditor.utils.filters.PixelateEffectFilter
import com.sun.imageeditor.utils.filters.SepiaEffectFilter
import com.sun.imageeditor.utils.filters.VignetteEffectFilter

class ImageProcessor(originalImage: Bitmap, private val context: Context) {

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

    private var mEditParameters = EditParameters()
    private val mIconList = mutableListOf<PointToDraw>()

    fun edit(editType: EditType, editParameters: EditParameters) {
        val editTypeIndex = mPipeLines.indexOfFirst { it.first == editType }

        mExecutor.execute {
            var previousImage: Bitmap

            for (i in editTypeIndex until mPipeLines.size) {
                previousImage = mPipeLines[i - 1].second

                when (mPipeLines[i].first) {
                    EditType.FILTER -> {
                        if (editType == EditType.FILTER) {
                            mEditParameters = mEditParameters.copy(
                                filterType = editParameters.filterType
                            )
                        }

                        onPipeline(previousImage, i, this::filter)
                    }
                    EditType.ADJUST -> {
                        if (editType == EditType.ADJUST) {
                            mEditParameters = mEditParameters.copy(
                                brightness = editParameters.brightness,
                                contrast = editParameters.contrast
                            )
                        }

                        onPipeline(previousImage, i, this::adjust)
                    }
                    EditType.CROP -> {
                        onPipeline(previousImage, i, this::crop)
                    }
                    EditType.DRAW -> {
                        onPipeline(previousImage, i, this::draw)
                    }
                    EditType.ICON -> {

                        editParameters.icon?.let {
                            mIconList.add(it)
                        }

                        val bitmap = icon(previousImage, mIconList, context)
                        mPipeLines[i] = mPipeLines[i].copy(second = bitmap)
                    }

                    else -> {}
                }
            }

            onResultListener?.onSuccess(mPipeLines.last().second)
        }
    }

    fun getFilterPreview(filterType: FilterType, listener: OnResultListener<Bitmap>) {
        mExecutor.execute {
            val originalIndex = mPipeLines.indexOfFirst { it.first == EditType.ORIGINAL }

            val bm = filter(
                mPipeLines[originalIndex].second.reducedBitmap(Constant.THUMBNAIL_REDUCE_SIZE),
                EditParameters(filterType = filterType)
            )

            listener.onSuccess(bm)
        }
    }

    private fun onPipeline(
        previousBitmap: Bitmap,
        index: Int,
        editFunction: (Bitmap, EditParameters) -> Bitmap,
    ) {
        val bitmap = editFunction(previousBitmap, mEditParameters)
        mPipeLines[index] = mPipeLines[index].copy(second = bitmap)
    }

    private fun filter(bitmap: Bitmap, editParameters: EditParameters): Bitmap {
        return when (editParameters.filterType) {
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

    private fun adjust(bitmap: Bitmap, editParameters: EditParameters) : Bitmap {
        var output = bitmap

        editParameters.brightness?.let {
            output = BrightnessAdjust().apply(bitmap, it)
        }
        editParameters.contrast?.let {
            output = ContrastAdjust().apply(output, it)
        }
        return output
    }

    private fun crop(bitmap: Bitmap, editParameters: EditParameters) : Bitmap {
        editParameters.crop?.let {
            // TODO
        }
        return bitmap
    }

    private fun draw(bitmap: Bitmap, editParameters: EditParameters) : Bitmap {
        editParameters.draw?.let {
            // TODO
        }
        return bitmap
    }

    private fun icon(
        bitmap: Bitmap,
        iconList: List<PointToDraw>,
        context: Context?
    ) : Bitmap {
        return if (context != null) {
            IconDraw().apply(bitmap, iconList, context)
        } else {
            bitmap
        }
    }


    fun saveBitmap(
        fileName: String?,
        context: Context?,
        onResultListener: OnResultListener<String>,
    ) {
        mExecutor.execute {
            ImageSaver().saveBitmap(
                mPipeLines.last().second,
                fileName,
                context,
                onResultListener
            )
        }
    }

    companion object {
        const val ERROR_MESSAGE = "Error in image processing"
    }
}
