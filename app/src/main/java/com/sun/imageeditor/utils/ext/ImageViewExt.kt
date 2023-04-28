package com.sun.imageeditor.utils.ext

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.IntegerRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

fun ImageView.loadImageWithUrl(url: String, placeHolder: Int) {
    Glide.with(this)
        .load(url)
        .placeholder(placeHolder)
        .into(this)
}

fun ImageView.loadOriginalImageWithUrl(
    url: String,
    @DrawableRes placeHolder: Int,
    @DrawableRes error: Int,
    onImageLoaded: ((Bitmap) -> Unit)? = null,
) {
    Glide.with(this)
        .asBitmap()
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .dontTransform()
        .placeholder(placeHolder)
        .error(error)
        .listener(object : RequestListener<Bitmap> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Bitmap>?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Bitmap?,
                model: Any?,
                target: Target<Bitmap>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                resource?.let { onImageLoaded?.invoke(it) }
                return false
            }

        })
        .into(this)
}
