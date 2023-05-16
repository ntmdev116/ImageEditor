package com.sun.imageeditor.utils

data class PointToDraw(
    val coordinate: Pair<Float, Float>,
    val redId: Int,
) {
    val x = coordinate.first
    val y = coordinate.second
}
