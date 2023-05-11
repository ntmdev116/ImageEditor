package com.sun.imageeditor.utils.ext

import android.graphics.Bitmap

fun Bitmap.reducedBitmap(percent: Float): Bitmap {
    val width = this.width
    val height = this.height
    val scaledWidth = (width * percent).toInt()
    val scaledHeight = (height * percent).toInt()
    return Bitmap.createScaledBitmap(this, scaledWidth, scaledHeight, true)
}
