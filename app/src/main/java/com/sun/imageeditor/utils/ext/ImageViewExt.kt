package com.sun.imageeditor.utils.ext

import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadImageWithUrl(url: String, placeHolder: Int) {
    Glide.with(this)
        .load(url)
        .placeholder(placeHolder)
        .into(this)
}
