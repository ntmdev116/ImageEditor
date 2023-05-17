package com.sun.imageeditor.utils

import com.sun.imageeditor.utils.draw.PathToDraw

data class EditParameters(
    val filterType: FilterType? = null,
    val adjustType: Int? = null,
    val brightness: Float? = null,
    val contrast: Float? = null,
    val icon: PointToDraw? = null,
    val crop: Float? = null,
    val draw: List<PathToDraw>? = null,
)
