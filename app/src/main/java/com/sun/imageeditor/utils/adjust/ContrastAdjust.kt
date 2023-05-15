package com.sun.imageeditor.utils.adjust

import android.graphics.Bitmap
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.Canvas
import androidx.annotation.FloatRange
import com.sun.imageeditor.utils.Constant

class ContrastAdjust : ImageAdjust{
    override fun apply(
        bitmap: Bitmap,
        @FloatRange(from = -1.0, to = 1.0) intensity: Float
    ): Bitmap {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)

        // when intensity is below 0, we need avoid adjust color to be black
        val contrast = if (intensity < 0) {
            intensity / Constant.ADJUST_MODIFIER + 1
        } else {
            intensity + 1
        }

        /*
        scale red, blue, green channel base on original color
        to make the dark color darker, the light color lighter
         */
        val colorMatrix = ColorMatrix().apply {
            setScale(contrast, contrast, contrast, 1f)
        }

        val paint = Paint().apply {
            colorFilter = ColorMatrixColorFilter(colorMatrix)
        }

        Canvas(output).drawBitmap(bitmap, 0f, 0f, paint)

        return output
    }
}
