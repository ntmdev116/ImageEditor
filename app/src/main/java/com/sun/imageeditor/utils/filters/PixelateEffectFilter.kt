package com.sun.imageeditor.utils.filters

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import com.sun.imageeditor.utils.Constant
import kotlin.math.ceil

class PixelateEffectFilter : ImageFilter {

    override fun apply(bitmap: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)

        val blockSize = Constant.PIXELATE_BLOCK_SIZE
        val width = bitmap.width
        val height = bitmap.height

        val numBlocksX = ceil(width.toDouble() / blockSize.toDouble()).toInt()
        val numBlocksY = ceil(height.toDouble() / blockSize.toDouble()).toInt()

        val canvas = Canvas(output)
        canvas.drawBitmap(bitmap, 0f, 0f, null)

        val paint = Paint()

        /*
        re-paint each blockSize x blockSize block with a color
        so give pixelate effect
        */
        for (xb in 0 until numBlocksX) {
            for (yb in 0 until numBlocksY) {
                val x = xb * blockSize
                val y = yb * blockSize

                val pixelColor = bitmap.getPixel(x, y)
                val blockBitmap = Bitmap.createBitmap(blockSize, blockSize, Bitmap.Config.ARGB_8888)
                blockBitmap.eraseColor(pixelColor)

                canvas.drawBitmap(blockBitmap, x.toFloat(), y.toFloat(), paint)
            }
        }

        return output
    }
}
