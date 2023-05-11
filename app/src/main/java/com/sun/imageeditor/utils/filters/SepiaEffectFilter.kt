package com.sun.imageeditor.utils.filters

import android.graphics.Bitmap
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.Canvas
import com.sun.imageeditor.utils.Constant

class SepiaEffectFilter : ImageFilter {

    override fun apply(bitmap: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)

        // with this value, color will be shifted to brown, give warm effect
        val colorMatrix = ColorMatrix(
            floatArrayOf(
                Constant.SEPIA_FIRST_RED, Constant.SEPIA_FIRST_GREEN, Constant.SEPIA_FIRST_BLUE, 0f, 0f,
                Constant.SEPIA_SECOND_RED, Constant.SEPIA_SECOND_GREEN, Constant.SEPIA_SECOND_BLUE, 0f, 0f,
                Constant.SEPIA_THIRD_RED, Constant.SEPIA_THIRD_GREEN, Constant.SEPIA_THIRD_BLUE, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
        )

        val paint = Paint()
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)

        val canvas = Canvas(output)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        return output
    }
}
