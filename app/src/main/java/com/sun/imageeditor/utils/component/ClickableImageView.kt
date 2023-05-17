package com.sun.imageeditor.utils.component

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Canvas
import android.graphics.Bitmap
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toBitmap
import com.sun.imageeditor.utils.Constant
import com.sun.imageeditor.utils.draw.PathToDraw


class ClickableImageView(context: Context, attrs: AttributeSet) : AppCompatImageView(context, attrs) {
    var size = 1
    var color = Color.BLACK
    var canDraw = false

    var newHeight = 1f
    var newWidth = 1f

    private var mBitmap = this.drawable.toBitmap()
    private var mImageAspect = 1f
    private var mViewAspect = 1f

    var path = Path()

    private val circlePaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 1f
    }

    private val mClickPaths = mutableListOf<PathToDraw>()
    val clickPaths = mutableListOf<PathToDraw>()


    private var mOnButtonDownListener: ((PointF) -> Unit)? = null

    fun setOnButtonDownListener(onTouchListener: (PointF) -> Unit) {
        mOnButtonDownListener = onTouchListener
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mOnButtonDownListener?.invoke(
                    getProjectedCoordinate(event.x, event.y)
                )

                path = Path()
                path.moveTo(event.x, event.y)
                mClickPaths.add(
                    PathToDraw(
                        path,
                        size.toFloat() * Constant.COLOR_DRAW_MODIFIER,
                        color
                    )
                )

                val projected = getProjectedCoordinate(event.x, event.y)
                clickPaths.add(mClickPaths.last().copy(
                    path = Path().apply { moveTo(projected.x, projected.y) },
                    size = size.toFloat() * Constant.COLOR_DRAW_MODIFIER * mBitmap.width / width
                ))
                true
            }
            MotionEvent.ACTION_MOVE -> {
                if (canDraw) {
                    val projected = getProjectedCoordinate(event.x, event.y)
                    clickPaths.last().path.lineTo(projected.x, projected.y)

                    path.lineTo(event.x, event.y)
                    invalidate()
                }

                false
            }
            else -> {
                super.onTouchEvent(event)
            }
        }
    }

    override fun onDrawForeground(canvas: Canvas?) {
        super.onDrawForeground(canvas)
        for (pathToDraw in mClickPaths) {
            canvas?.drawPath(
                pathToDraw.path,
                circlePaint.apply {
                    color = pathToDraw.color
                    strokeWidth = pathToDraw.size
                }
            )

        }
    }

    // project fitCenter scaleType ImageView's touch coordinate to its bitmap
    private fun getProjectedCoordinate(x: Float, y: Float) : PointF {
        val isBitmapBigger = mImageAspect > mViewAspect

        val projectedX = if (isBitmapBigger) {
            x * mBitmap.width / this.width
        } else {
            (x - (this.width - newWidth) / Constant.TWO) * mBitmap.width / newWidth
        }

        val projectedY = if (isBitmapBigger) {
            (y - (this.height - newHeight) / Constant.TWO) * mBitmap.height / newHeight
        } else {
            y * mBitmap.height / this.height
        }

        return PointF(projectedX, projectedY)
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)

        bm?.let {
            calculateBitmapSize()
        }
    }

    private fun calculateBitmapSize() {
        mBitmap = drawable.toBitmap()

        mImageAspect = mBitmap.width / mBitmap.height.toFloat()
        mViewAspect = this.width / this.height.toFloat()

        newWidth = if (mImageAspect > mViewAspect) {
            this.width.toFloat()
        } else {
            this.height.toFloat() * mImageAspect
        }

        newHeight = if (mImageAspect > mViewAspect) {
            newWidth / mImageAspect
        } else {
            this.height.toFloat()
        }
    }
}
