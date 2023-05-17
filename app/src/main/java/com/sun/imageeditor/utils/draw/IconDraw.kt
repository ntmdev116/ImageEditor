package com.sun.imageeditor.utils.draw

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import com.sun.imageeditor.utils.Constant
import com.sun.imageeditor.utils.PointToDraw

class IconDraw : ImageDraw {
    override fun apply(
        bitmap: Bitmap,
        drawPoint: List<PointToDraw>,
        context: Context
    ): Bitmap {
        val output = bitmap.copy(Bitmap.Config.ARGB_8888, true)

        val canvas = Canvas(output)

        for ((coordinate, icon) in drawPoint) {
            val iconBitmap = BitmapFactory.decodeResource(context.resources, icon)

            val centerCoordinate = Pair(
                coordinate.x - iconBitmap.width / Constant.TWO,
                coordinate.y - iconBitmap.height / Constant.TWO,
            )

            if (centerCoordinate.first > 0 && centerCoordinate.second > 0) {
                canvas.drawBitmap(
                    iconBitmap,
                    centerCoordinate.first,
                    centerCoordinate.second,
                    null
                )
            }
        }

        return output
    }
}
