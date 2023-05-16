package com.sun.imageeditor.utils.draw

import android.content.Context
import android.graphics.Bitmap
import com.sun.imageeditor.utils.PointToDraw

interface ImageDraw {
    fun apply(
        bitmap: Bitmap,
        drawPoint: List<PointToDraw>,
        context: Context
    ): Bitmap
}
