package com.sun.imageeditor.utils.ext

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.IntegerRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

fun ImageView.loadImageWithUrl(url: String, placeHolder: Int) {
    Glide.with(this)
        .load(url)
        .placeholder(placeHolder)
        .into(this)
}

fun ImageView.loadOriginalImageWithUrl(
    url: String,
    @DrawableRes placeHolder: Int,
    @DrawableRes error: Int
) {
    Glide.with(this)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .dontTransform()
        .placeholder(placeHolder)
        .error(error)
        .into(this)
}
