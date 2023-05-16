package com.sun.imageeditor.utils.component

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toBitmap
import com.sun.imageeditor.utils.Constant


class ClickableImageView(context: Context, attrs: AttributeSet) : AppCompatImageView(context, attrs) {
    private var mOnButtonDownListener: ((Pair<Float, Float>) -> Unit)? = null

    fun setOnButtonDownListener(onTouchListener: (Pair<Float, Float>) -> Unit) {
        mOnButtonDownListener = onTouchListener
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mOnButtonDownListener?.invoke(
                    getProjectedCoordinate(event.x, event.y)
                )

                true
            }
            else -> {
                super.onTouchEvent(event)
            }
        }
    }

    // project fitCenter scaleType ImageView's touch coordinate to its bitmap
    private fun getProjectedCoordinate(x: Float, y: Float) : Pair<Float, Float> {
        val drawable = this.drawable
        val bitmap = drawable.toBitmap()

        val imageAspect = bitmap.width / bitmap.height.toFloat()
        val viewAspect = this.width / this.height.toFloat()

        val newWidth = if (imageAspect > viewAspect) {
            this.width.toFloat()
        } else {
            this.height.toFloat() * imageAspect
        }

        val newHeight = if (imageAspect > viewAspect) {
            newWidth / imageAspect
        } else {
            this.height.toFloat()
        }

        return if (imageAspect > viewAspect) {
            val projectedX = x * bitmap.width / this.width
            val projectedY =
                (y - (this.height - newHeight) / Constant.TWO) * bitmap.height / newHeight

            Pair(projectedX, projectedY)
        } else {
            val projectedX =
                (x - (this.width - newWidth) / Constant.TWO) * bitmap.width / newWidth
            val projectedY = y * bitmap.height / this.height

            Pair(projectedX, projectedY)
        }

    }
}
