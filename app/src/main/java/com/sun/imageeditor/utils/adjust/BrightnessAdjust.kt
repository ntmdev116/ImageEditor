package com.sun.imageeditor.utils.adjust

import android.graphics.Bitmap
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.Canvas
import androidx.annotation.FloatRange
import com.sun.imageeditor.utils.Constant

class BrightnessAdjust : ImageAdjust {
    override fun apply(
        bitmap: Bitmap,
        @FloatRange(from = -1.0, to = 1.0) intensity: Float
    ): Bitmap {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)

        // adjust brightness to not be a black or white image
        val brightness =
            Constant.MAX_COLOR_PIXEL * intensity / Constant.ADJUST_MODIFIER

        // increase each value of red, green, blue color by brightness
        val colorMatrix = ColorMatrix(
            floatArrayOf(
                1f, 0f, 0f, 0f, brightness,
                0f, 1f, 0f, 0f, brightness,
                0f, 0f, 1f, 0f, brightness,
                0f, 0f, 0f, 1f, 0f
            )
        )

        val paint = Paint().apply {
            colorFilter = ColorMatrixColorFilter(colorMatrix)
        }

        Canvas(output).drawBitmap(bitmap, 0f, 0f, paint)

        return output
    }
}
