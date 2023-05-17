package com.sun.imageeditor.utils.draw

import android.graphics.Path
import androidx.annotation.ColorInt

data class PathToDraw(
    val path: Path,
    val size: Float,
    @ColorInt val color: Int,
)
