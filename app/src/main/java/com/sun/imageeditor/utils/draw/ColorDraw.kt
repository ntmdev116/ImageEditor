package com.sun.imageeditor.utils.draw

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint

class ColorDraw {
    fun apply(
        bitmap: Bitmap,
        drawPaths: List<PathToDraw>,
    ): Bitmap {
        val output = bitmap.copy(Bitmap.Config.ARGB_8888, true)

        val canvas = Canvas(output)

        for (drawPath in drawPaths) {
            draw(canvas, drawPath)
        }

        return output
    }

    private fun draw(canvas: Canvas, drawPath: PathToDraw) {

        val paint = Paint().apply {
            color = drawPath.color
            style = Paint.Style.STROKE
            strokeWidth = drawPath.size
        }

        canvas.drawPath(drawPath.path, paint)
    }
}
