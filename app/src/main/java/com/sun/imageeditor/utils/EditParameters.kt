package com.sun.imageeditor.utils

data class EditParameters(
    val filterType: FilterType? = null,
    val adjustType: Int? = null,
    val brightness: Float? = null,
    val contrast: Float? = null,
    // these properties for testing
    val icon: PointToDraw? = null,
    val crop: Float? = null,
    val draw: Float? = null,
)
