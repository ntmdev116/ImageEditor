package com.sun.imageeditor.utils.adjust

import android.graphics.Bitmap

interface ImageAdjust {
    fun apply(bitmap: Bitmap, intensity: Float) : Bitmap
}
