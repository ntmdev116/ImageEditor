package com.sun.imageeditor.utils.filters

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.RadialGradient
import android.graphics.Color
import android.graphics.Shader
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import com.sun.imageeditor.utils.Constant


class VignetteEffectFilter : ImageFilter {
    override fun apply(bitmap: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)

        /*
        position and color to draw circle for this effect
        this will look like a gradient circle
         */
        val positionToColor = listOf<Pair<Float, Int>>(
            Pair(Constant.VIGNETTE_FIRST_POSITION, Color.argb(Constant.VIGNETTE_FIRST_ALPHA, 0, 0, 0)),
            Pair(Constant.VIGNETTE_SECOND_POSITION, Color.argb(Constant.VIGNETTE_SECOND_ALPHA, 0, 0, 0)),
            Pair(Constant.VIGNETTE_THIRD_POSITION, Color.argb(Constant.VIGNETTE_THIRD_ALPHA, 0, 0, 0)),
            Pair(Constant.VIGNETTE_FORTH_POSITION, Color.argb(Constant.VIGNETTE_FORTH_ALPHA, 0, 0, 0)),
            Pair(Constant.VIGNETTE_FIFTH_POSITION, Color.argb(Constant.VIGNETTE_FIFTH_ALPHA, 0, 0, 0)),
            Pair(Constant.VIGNETTE_SIXTH_POSITION, Color.argb(Constant.VIGNETTE_SIXTH_ALPHA, 0, 0, 0)),
            Pair(Constant.VIGNETTE_SEVENTH_POSITION, Color.argb(Constant.VIGNETTE_SEVENTH_ALPHA, 0, 0, 0)),
            Pair(Constant.VIGNETTE_EIGHTH_POSITION, Color.argb(Constant.VIGNETTE_EIGHTH_ALPHA, 0, 0, 0)),
        )

        val positions = positionToColor.map { it.first }.toFloatArray()
        val colors = positionToColor.map {it.second}.toIntArray()
        val radius = bitmap.width.toFloat()

        val gradient = RadialGradient(
            bitmap.width * Constant.VIGNETTE_CENTER_MODIFIER,
            bitmap.height * Constant.VIGNETTE_CENTER_MODIFIER,
            radius,
            colors,
            positions,
            Shader.TileMode.CLAMP
        )

        val canvas = Canvas(output)
        canvas.drawARGB(1, 0, 0, 0)

        val paint = Paint()
        paint.isAntiAlias = true
        paint.color = Color.BLACK
        paint.shader = gradient

        val rect = Rect(0, 0, output.width, output.height)
        val rectF = RectF(rect)

        canvas.drawRect(rectF, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
    }
}
