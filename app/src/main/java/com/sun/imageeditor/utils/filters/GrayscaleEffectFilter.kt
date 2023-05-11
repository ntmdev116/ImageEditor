package com.sun.imageeditor.utils.filters

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint

class GrayscaleEffectFilter : ImageFilter {
    override fun apply(bitmap: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        val paint = Paint()
        val colorMatrix = ColorMatrix().apply {
            setSaturation(0f)
        }
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)

        val canvas = Canvas(output)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        return output
    }
}
