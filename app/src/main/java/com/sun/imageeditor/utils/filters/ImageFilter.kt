package com.sun.imageeditor.utils.filters

import android.graphics.Bitmap

interface ImageFilter {
    fun apply(bitmap: Bitmap): Bitmap
}
